package com.dailytodocalendar.api.memo.repository;

import com.dailytodocalendar.api.memo.dto.CalendarDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import com.dailytodocalendar.api.memo.entity.Memo;
import com.dailytodocalendar.api.memo.entity.QMemo;
import com.dailytodocalendar.api.memo.entity.QTodo;
import com.dailytodocalendar.api.memo.entity.Todo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MemoDto> findAllByMemberIdAndDate(Long memberId, Integer year, LocalDate date) {
    List<Memo> memos =
        queryFactory
            .selectFrom(QMemo.memo)
            .leftJoin(QMemo.memo.todos, QTodo.todo)
            .fetchJoin()
            .where(
                QMemo.memo.memberId.eq(memberId),
                year != null ? QMemo.memo.scheduleDate.year().eq(year) : null,
                date != null ? QMemo.memo.scheduleDate.eq(date) : null)
            .fetch();

    return memos.stream()
        .map(
            memo ->
                MemoDto.builder()
                    .id(memo.getId())
                    .memberId(memo.getMemberId())
                    .title(memo.getTitle())
                    .content(memo.getContent())
                    .todos(memo.getTodos().stream().map(Todo::toDto).collect(Collectors.toList()))
                    .scheduleDate(memo.getScheduleDate())
                    .regDate(memo.getRegDate())
                    .udtDate(memo.getUdtDate())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId) {
    return queryFactory
        .select(
            Projections.fields(
                CalendarDto.class,
                QMemo.memo.scheduleDate.as("scheduleDate"),
                QTodo.todo.id.count().as("totalCnt"),
                new CaseBuilder()
                    .when(QTodo.todo.completed.eq(true))
                    .then(1L)
                    .otherwise(0L)
                    .sum()
                    .as("successCnt"))) // 조건부 카운트를 바로 select 절에서 정의
        .from(QMemo.memo)
        .leftJoin(QTodo.todo)
        .on(QMemo.memo.id.eq(QTodo.todo.memo.id))
        .where(
            QMemo.memo.memberId.eq(memberId),
            year != null
                ? QMemo.memo.scheduleDate.year().eq(year)
                : QMemo.memo.scheduleDate.year().isNull())
        .groupBy(QMemo.memo.scheduleDate)
        .orderBy(QMemo.memo.scheduleDate.asc())
        .fetch();
  }
}
