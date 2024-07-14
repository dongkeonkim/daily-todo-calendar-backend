package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.dto.CalendarDto;
import com.dailytodocalendar.memo.entity.Memo;
import com.dailytodocalendar.memo.entity.QMemo;
import com.dailytodocalendar.memo.entity.QTodo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Memo> findAllByMemberIdAndDate(Long memberId, Integer year, LocalDate date) {
        return queryFactory
                .selectFrom(QMemo.memo)
                .where(
                        QMemo.memo.memberId.eq(memberId),
                        year != null ? QMemo.memo.scheduleDate.year().eq(year) : QMemo.memo.scheduleDate.year().isNull(),
                        date != null ? QMemo.memo.scheduleDate.eq(date) : null
                )
                .fetch();
    }

    @Override
    public List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId) {
        return queryFactory
                .select(Projections.fields(CalendarDto.class,
                        QMemo.memo.scheduleDate.as("scheduleDate"),
                        QTodo.todo.id.count().as("totalCnt"),
                        new CaseBuilder()
                                .when(QTodo.todo.completed.eq(true))
                                .then(1L)
                                .otherwise(0L)
                                .sum()
                                .as("successCnt"))) // 조건부 카운트를 바로 select 절에서 정의
                .from(QMemo.memo)
                .leftJoin(QTodo.todo).on(QMemo.memo.id.eq(QTodo.todo.memo.id))
                .where(
                        QMemo.memo.memberId.eq(memberId),
                        year != null ? QMemo.memo.scheduleDate.year().eq(year) : QMemo.memo.scheduleDate.year().isNull()
                )
                .groupBy(QMemo.memo.scheduleDate)
                .orderBy(QMemo.memo.scheduleDate.asc())
                .fetch();
    }

}