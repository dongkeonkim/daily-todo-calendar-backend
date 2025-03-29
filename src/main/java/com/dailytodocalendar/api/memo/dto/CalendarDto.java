package com.dailytodocalendar.api.memo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate scheduleDate;

  Long successCnt;
  Long totalCnt;

  @Override
  public String toString() {
    return "CalendarDto{"
        + "scheduleDate="
        + scheduleDate
        + ", successCnt="
        + successCnt
        + ", totalCnt="
        + totalCnt
        + '}';
  }
}
