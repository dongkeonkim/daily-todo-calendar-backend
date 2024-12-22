package com.dailytodocalendar.api.memo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalendarListDto {

    private List<CalendarDto> calendar;
    private List<String> years;

}
