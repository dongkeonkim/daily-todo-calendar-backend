package com.dailytodocalendar.memo.repository;

import com.dailytodocalendar.memo.dto.CalendarDto;

import java.util.List;

public interface MemoRepositoryCustom {

    List<CalendarDto> getTodoCountInCalendar(int year, long memberId);

}
