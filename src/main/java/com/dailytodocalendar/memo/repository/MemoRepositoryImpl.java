package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.dto.CalendarDto;
import com.dailytodocalendar.memo.entity.QMemo;
import com.dailytodocalendar.memo.entity.QTodo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalendarDto> getTodoCountInCalendar(int year, long memberId) {
        QMemo memo = QMemo.memo;
        QTodo todo = QTodo.todo;

        return queryFactory
                .select(Projections.fields(CalendarDto.class,
                        memo.scheduleDate.as("scheduleDate"),
                        todo.id.count().as("totalCnt"),
                        new CaseBuilder()
                                .when(todo.completed.eq(true))
                                .then(1L)
                                .otherwise(0L)
                                .sum()
                                .as("successCnt"))) // 조건부 카운트를 바로 select 절에서 정의
                .from(memo)
                .leftJoin(todo).on(memo.id.eq(todo.memo.id))
                .where(memo.scheduleDate.isNotNull())
                .groupBy(memo.scheduleDate)
                .orderBy(memo.scheduleDate.desc())
                .fetch();
    }
}