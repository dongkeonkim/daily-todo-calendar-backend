package com.dailytodocalendar.application.port.in.memo;

import com.dailytodocalendar.application.port.in.memo.dto.CalendarListDto;
import com.dailytodocalendar.application.port.in.memo.dto.MemoDto;
import java.time.LocalDate;
import java.util.List;

/** 메모 조회 유스케이스 */
public interface GetMemoUseCase {

  /**
   * 메모 목록 조회
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @param date 날짜 (선택)
   * @return 메모 DTO 목록
   */
  List<MemoDto> getMemos(Long memberId, Integer year, Integer month, LocalDate date);

  /**
   * 캘린더 데이터 조회 (할일 통계)
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 데이터 목록
   */
  CalendarListDto getCalendarData(Long memberId, Integer year, Integer month);
}
