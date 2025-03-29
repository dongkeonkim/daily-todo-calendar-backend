package com.dailytodocalendar.api.memo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarListDto {

  private List<CalendarDto> calendar;
  private List<String> years;
}
