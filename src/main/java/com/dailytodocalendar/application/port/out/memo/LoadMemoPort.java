package com.dailytodocalendar.application.port.out.memo;

import com.dailytodocalendar.application.port.in.memo.dto.CalendarDto;
import com.dailytodocalendar.domain.memo.model.Memo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** 메모 조회 포트 */
public interface LoadMemoPort {

  /**
   * ID로 메모 조회
   *
   * @param id 메모 ID
   * @return 메모 엔티티 옵셔널
   */
  Optional<Memo> findById(Long id);

  /**
   * 회원 ID와 메모 ID로 메모 조회
   *
   * @param id 메모 ID
   * @param memberId 회원 ID
   * @return 메모 엔티티 옵셔널
   */
  Optional<Memo> findByIdAndMemberId(Long id, Long memberId);

  /**
   * 회원의 메모 목록 조회 (필터링 가능)
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @param date 날짜 (선택)
   * @return 메모 엔티티 목록
   */
  List<Memo> findAllByMemberIdAndDate(Long memberId, Integer year, Integer month, LocalDate date);

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
}
