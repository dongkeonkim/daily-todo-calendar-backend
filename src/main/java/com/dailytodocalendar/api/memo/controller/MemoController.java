package com.dailytodocalendar.api.memo.controller;

import com.dailytodocalendar.api.memo.dto.CalendarListDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import com.dailytodocalendar.api.memo.service.MemoService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.common.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
@Validated
@Tag(name = "Memo API", description = "메모 및 할일 관리 API")
public class MemoController {

  private final MemoService memoService;

  @GetMapping("")
  @Operation(summary = "메모 조회", description = "연도와 날짜별 메모 목록을 조회합니다.")
  public ResponseDto<List<MemoDto>> selectMemoAll(
      @Parameter(description = "조회할 연도 (선택사항)") @RequestParam(required = false, value = "year")
          Integer year,
      @Parameter(description = "조회할 날짜 (선택사항)")
          @RequestParam(required = false, value = "date")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate date) {

    log.debug("메모 조회 요청 - 연도: {}, 날짜: {}", year, date);
    return ResponseDto.success(
        SuccessCode.SUCCESS, memoService.selectMemoAll(year, date, SecurityUtils.getCurrentUser()));
  }

  @GetMapping("/calendar")
  @Operation(summary = "캘린더 데이터 조회", description = "캘린더에 표시할 할일 통계 데이터를 조회합니다.")
  public ResponseDto<CalendarListDto> getTodoCountInCalendar(
      @Parameter(description = "조회할 연도 (선택사항)") @RequestParam(required = false, value = "year")
          Integer year) {

    log.debug("캘린더 데이터 조회 요청 - 연도: {}", year);
    Long memberId = SecurityUtils.getCurrentUserId();

    CalendarListDto calendarListDto = new CalendarListDto();
    calendarListDto.setCalendar(memoService.getTodoCountInCalendar(year, memberId));
    calendarListDto.setYears(memoService.getTodoCompleteYears(memberId));

    return ResponseDto.success(SuccessCode.SUCCESS, calendarListDto);
  }

  @PostMapping("/create")
  @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
  public ResponseDto<MemoDto> createMemo(@Valid @RequestBody MemoDto memoDto) {
    log.debug("메모 생성 요청: {}", memoDto);
    return ResponseDto.success(
        SuccessCode.MEMO_CREATED, memoService.createMemo(memoDto, SecurityUtils.getCurrentUser()));
  }

  @PutMapping("/update")
  @Operation(summary = "메모 수정", description = "기존 메모를 수정합니다.")
  public ResponseDto<MemoDto> updateMemo(@Valid @RequestBody MemoDto memoDto) {
    log.debug("메모 수정 요청: {}", memoDto);
    return ResponseDto.success(
        SuccessCode.MEMO_UPDATED, memoService.updateMemo(memoDto, SecurityUtils.getCurrentUser()));
  }

  @DeleteMapping("/delete/{id}")
  @Operation(summary = "메모 삭제", description = "메모를 삭제합니다.")
  public ResponseDto<Void> deleteMemo(
      @Parameter(description = "삭제할 메모 ID")
          @PathVariable("id")
          @Min(value = 1, message = "메모 ID는 1 이상이어야 합니다.")
          Long id) {

    log.debug("메모 삭제 요청 - ID: {}", id);
    memoService.deleteMemo(id, SecurityUtils.getCurrentUser());
    return ResponseDto.success(SuccessCode.MEMO_DELETED);
  }
}
