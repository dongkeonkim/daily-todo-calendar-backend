package com.dailytodocalendar.application.port.in.memo.dto;

import java.time.LocalDate;

/** 캘린더 DTO - 특정 날짜의 할일 통계 정보 */
public record CalendarDto(LocalDate scheduleDate, Long totalCount, Long completedCount) {}
