package com.dailytodocalendar.adapter.out.persistence.memo;

import com.dailytodocalendar.application.port.in.memo.dto.CalendarDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

/** 메모 사용자 정의 리포지토리 구현 - 성능 최적화 */
@Repository
public class MemoRepositoryImpl implements MemoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  /** QueryDSL용 Q 클래스 */
  private final QMemoJpaEntity memo = QMemoJpaEntity.memoJpaEntity;

  private final QTodoJpaEntity todo = QTodoJpaEntity.todoJpaEntity;

  public MemoRepositoryImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  /**
   * 캘린더용 할일 통계 조회
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 DTO 목록
   */
  @Override
  public List<CalendarDto> getTodoCountInCalendar(Long memberId, Integer year, Integer month) {
    // 조건 생성
    BooleanExpression memberCondition = memo.memberId.eq(memberId);
    BooleanExpression yearCondition = year != null ? memo.scheduleDate.year().eq(year) : null;
    BooleanExpression monthCondition = month != null ? memo.scheduleDate.month().eq(month) : null;

    // 조건 결합
    BooleanExpression condition = memberCondition;
    if (yearCondition != null) {
      condition = condition.and(yearCondition);
    }
    if (monthCondition != null) {
      condition = condition.and(monthCondition);
    }

    // 쿼리 실행 - DTO로 직접 매핑하여 성능 개선
    return queryFactory
        .select(
            Projections.constructor(
                CalendarDto.class,
                memo.scheduleDate,
                todo.count().as("totalCount"),
                todo.completed.when(true).then(1L).otherwise(0L).sum().as("completedCount")))
        .from(memo)
        .leftJoin(todo)
        .on(todo.memo.eq(memo))
        .where(condition)
        .groupBy(memo.scheduleDate)
        .fetch();
  }

  /**
   * 회원의 할일이 있는 연도 목록 조회 - Redis 캐싱 적용
   *
   * @param memberId 회원 ID
   * @return 연도 목록
   */
  @Override
  public List<String> getTodoYears(Long memberId) {
    return queryFactory
        .select(memo.scheduleDate.year().stringValue())
        .from(memo)
        .where(memo.memberId.eq(memberId), memo.scheduleDate.isNotNull())
        .groupBy(memo.scheduleDate.year())
        .orderBy(memo.scheduleDate.year().desc())
        .fetch();
  }

  /**
   * 특정 날짜 범위의 메모 조회 - 단일 조회로 N+1 문제 해결
   *
   * @param memberId 회원 ID
   * @param startDate 시작 날짜
   * @param endDate 종료 날짜
   * @return 메모 엔티티 리스트
   */
  @Override
  public List<MemoJpaEntity> findByMemberIdAndDateRange(
      Long memberId, LocalDate startDate, LocalDate endDate) {
    return queryFactory
        .selectFrom(memo)
        .leftJoin(memo.todos, todo)
        .fetchJoin()
        .where(
            memo.memberId
                .eq(memberId)
                .and(memo.scheduleDate.goe(startDate))
                .and(memo.scheduleDate.loe(endDate)))
        .orderBy(memo.scheduleDate.asc())
        .distinct()
        .fetch();
  }
}
