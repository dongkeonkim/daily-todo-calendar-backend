package com.dailytodocalendar.adapter.out.persistence.memo;

import com.dailytodocalendar.application.port.in.memo.dto.CalendarDto;
import java.time.LocalDate;
import java.util.List;

/** 메모 사용자 정의 리포지토리 인터페이스 - 성능 최적화 버전 */
public interface MemoRepositoryCustom {

  /**
   * 캘린더용 할일 통계 조회
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 DTO 목록
   */
  List<CalendarDto> getTodoCountInCalendar(Long memberId, Integer year, Integer month);

  /**
   * 회원의 할일이 있는 연도 목록 조회
   *
   * @param memberId 회원 ID
   * @return 연도 목록
   */
  List<String> getTodoYears(Long memberId);

  /**
   * 특정 날짜 범위의 메모 조회 - N+1 문제 해결을 위한 조회 메서드
   *
   * @param memberId 회원 ID
   * @param startDate 시작 날짜
   * @param endDate 종료 날짜
   * @return 메모 엔티티 리스트
   */
  List<MemoJpaEntity> findByMemberIdAndDateRange(
      Long memberId, LocalDate startDate, LocalDate endDate);
}
