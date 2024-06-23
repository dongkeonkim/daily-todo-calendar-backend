package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.dto.CalendarDto;
import com.dailytodocalendar.memo.entity.Memo;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemoRepositoryCustom {

    List<Memo> findAllByMemberIdAndDate(@Param("memberId") Long memberId, @Param("year") Integer year, @Param("date") LocalDate date);

    List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId);

}
