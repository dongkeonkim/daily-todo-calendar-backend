package com.dailytodocalendar.adapter.in.web.memo;

import com.dailytodocalendar.adapter.in.web.common.CurrentUser;
import com.dailytodocalendar.adapter.in.web.common.WebAdapter;
import com.dailytodocalendar.application.port.in.memo.CreateMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.DeleteMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.GetMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.UpdateMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.dto.CalendarListDto;
import com.dailytodocalendar.application.port.in.memo.dto.MemoDto;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.infrastructure.config.security.custom.CustomUser;
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

/** 메모 컨트롤러 - 인바운드 어댑터 */
@Slf4j
@WebAdapter
@RequestMapping("/memo")
@RequiredArgsConstructor
@Validated
@Tag(name = "Memo API", description = "메모 및 할일 관리 API")
public class MemoController {

  private final GetMemoUseCase getMemoUseCase;
  private final CreateMemoUseCase createMemoUseCase;
  private final UpdateMemoUseCase updateMemoUseCase;
  private final DeleteMemoUseCase deleteMemoUseCase;

  /**
   * 메모 목록 조회
   *
   * @param customUser 인증된 사용자
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @param date 날짜 (선택)
   * @return 메모 목록
   */
  @GetMapping("")
  @Operation(summary = "메모 조회", description = "연도, 월, 날짜별 메모 목록을 조회합니다.")
  public ResponseDto<List<MemoDto>> getMemos(
      @CurrentUser CustomUser customUser,
      @Parameter(description = "조회할 연도 (선택사항, 전체=null)")
          @RequestParam(required = false, value = "year")
          Integer year,
      @Parameter(description = "조회할 월 (선택사항, 전체=null)")
          @RequestParam(required = false, value = "month")
          Integer month,
      @Parameter(description = "조회할 날짜 (선택사항)")
          @RequestParam(required = false, value = "date")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate date) {

    log.debug("메모 조회 요청 - 연도: {}, 월: {}, 날짜: {}", year, month, date);
    Long memberId = customUser.getMemberId();
    List<MemoDto> memos = getMemoUseCase.getMemos(memberId, year, month, date);
    return ResponseDto.success(SuccessCode.SUCCESS, memos);
  }

  /**
   * 캘린더 데이터 조회
   *
   * @param customUser 인증된 사용자
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 데이터
   */
  @GetMapping("/calendar")
  @Operation(summary = "캘린더 데이터 조회", description = "캘린더에 표시할 할일 통계 데이터를 조회합니다.")
  public ResponseDto<CalendarListDto> getCalendarData(
      @CurrentUser CustomUser customUser,
      @Parameter(description = "조회할 연도 (선택사항, 전체=null)")
          @RequestParam(required = false, value = "year")
          Integer year,
      @Parameter(description = "조회할 월 (선택사항, 전체=null)")
          @RequestParam(required = false, value = "month")
          Integer month) {

    log.debug("캘린더 데이터 조회 요청 - 연도: {}, 월: {}", year, month);
    Long memberId = customUser.getMemberId();
    CalendarListDto calendarData = getMemoUseCase.getCalendarData(memberId, year, month);
    return ResponseDto.success(SuccessCode.SUCCESS, calendarData);
  }

  /**
   * 메모 생성
   *
   * @param customUser 인증된 사용자
   * @param memoDto 메모 정보
   * @return 생성된 메모
   */
  @PostMapping("/create")
  @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
  public ResponseDto<MemoDto> createMemo(
      @CurrentUser CustomUser customUser, @Valid @RequestBody MemoDto memoDto) {

    log.debug("메모 생성 요청: {}", memoDto);
    Long memberId = customUser.getMemberId();
    MemoDto createdMemo = createMemoUseCase.createMemo(memoDto, memberId);
    return ResponseDto.success(SuccessCode.MEMO_CREATED, createdMemo);
  }

  /**
   * 메모 수정
   *
   * @param customUser 인증된 사용자
   * @param memoDto 메모 정보
   * @return 수정된 메모
   */
  @PutMapping("/update")
  @Operation(summary = "메모 수정", description = "기존 메모를 수정합니다.")
  public ResponseDto<MemoDto> updateMemo(
      @CurrentUser CustomUser customUser, @Valid @RequestBody MemoDto memoDto) {

    log.debug("메모 수정 요청: {}", memoDto);
    Long memberId = customUser.getMemberId();
    MemoDto updatedMemo = updateMemoUseCase.updateMemo(memoDto, memberId);
    return ResponseDto.success(SuccessCode.MEMO_UPDATED, updatedMemo);
  }

  /**
   * 메모 삭제
   *
   * @param customUser 인증된 사용자
   * @param id 메모 ID
   * @return 응답 객체
   */
  @DeleteMapping("/delete/{id}")
  @Operation(summary = "메모 삭제", description = "메모를 삭제합니다.")
  public ResponseDto<Void> deleteMemo(
      @CurrentUser CustomUser customUser,
      @Parameter(description = "삭제할 메모 ID")
          @PathVariable("id")
          @Min(value = 1, message = "메모 ID는 1 이상이어야 합니다.")
          Long id) {

    log.debug("메모 삭제 요청 - ID: {}", id);
    Long memberId = customUser.getMemberId();
    deleteMemoUseCase.deleteMemo(id, memberId);
    return ResponseDto.success(SuccessCode.MEMO_DELETED);
  }
}
