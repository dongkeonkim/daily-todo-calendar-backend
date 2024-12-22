package com.dailytodocalendar.api.memo.repository;

import com.dailytodocalendar.api.memo.dto.CalendarDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepositoryCustom {

    List<MemoDto> findAllByMemberIdAndDate(@Param("memberId") Long memberId, @Param("year") Integer year, @Param("date") LocalDate date);

    List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId);

}
