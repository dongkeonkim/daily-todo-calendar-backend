package com.dailytodocalendar.application.port.in.memo.dto;

import java.util.List;
import lombok.Builder;

/** 캘린더 목록 DTO - 캘린더 데이터와 연도 목록 포함 */
@Builder
public record CalendarListDto(List<CalendarDto> calendar, List<String> years) {}
