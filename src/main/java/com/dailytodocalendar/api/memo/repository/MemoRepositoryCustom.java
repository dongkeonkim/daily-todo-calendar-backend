package com.dailytodocalendar.api.memo.repository;

import com.dailytodocalendar.api.memo.dto.CalendarDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface MemoRepositoryCustom {

  List<MemoDto> findAllByMemberIdAndDate(
      @Param("memberId") Long memberId,
      @Param("year") Integer year,
      @Param("month") Integer month,
      @Param("date") LocalDate date);

  List<CalendarDto> getTodoCountInCalendar(Integer year, Integer month, long memberId);
}
