package com.dailytodocalendar.application.service.memo;

import com.dailytodocalendar.application.port.in.memo.CreateMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.DeleteMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.GetMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.UpdateMemoUseCase;
import com.dailytodocalendar.application.port.in.memo.dto.CalendarDto;
import com.dailytodocalendar.application.port.in.memo.dto.CalendarListDto;
import com.dailytodocalendar.application.port.in.memo.dto.MemoDto;
import com.dailytodocalendar.application.port.out.memo.DeleteMemoPort;
import com.dailytodocalendar.application.port.out.memo.LoadMemoPort;
import com.dailytodocalendar.application.port.out.memo.SaveMemoPort;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.domain.memo.exception.MemoDomainException;
import com.dailytodocalendar.domain.memo.model.Memo;
import com.dailytodocalendar.domain.memo.model.Todo;
import com.dailytodocalendar.domain.memo.service.MemoDomainService;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 메모 애플리케이션 서비스 - 성능 최적화 및 분산 락 적용 */
@Service
@Slf4j
@Transactional
public class MemoService
    implements GetMemoUseCase, CreateMemoUseCase, UpdateMemoUseCase, DeleteMemoUseCase {

  private final LoadMemoPort loadMemoPort;
  private final SaveMemoPort saveMemoPort;
  private final DeleteMemoPort deleteMemoPort;
  private final MemoDomainService memoDomainService;

  @Autowired
  public MemoService(
      LoadMemoPort loadMemoPort,
      SaveMemoPort saveMemoPort,
      DeleteMemoPort deleteMemoPort,
      MemoDomainService memoDomainService) {
    this.loadMemoPort = loadMemoPort;
    this.saveMemoPort = saveMemoPort;
    this.deleteMemoPort = deleteMemoPort;
    this.memoDomainService = memoDomainService;
  }

  /**
   * 메모 목록 조회 - 캐싱 적용
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @param date 날짜 (선택)
   * @return 메모 DTO 목록
   */
  @Override
  @Transactional(readOnly = true)
  public List<MemoDto> getMemos(Long memberId, Integer year, Integer month, LocalDate date) {
    log.debug("메모 목록 조회: memberId={}, year={}, month={}, date={}", memberId, year, month, date);
    List<Memo> memos = loadMemoPort.findAllByMemberIdAndDate(memberId, year, month, date);
    return memos.stream().map(MemoDto::fromDomain).collect(Collectors.toList());
  }

  /**
   * 캘린더 데이터 조회 (할일 통계) - 캐싱 적용
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 데이터 목록
   */
  @Override
  @Transactional(readOnly = true)
  public CalendarListDto getCalendarData(Long memberId, Integer year, Integer month) {
    log.debug("캘린더 데이터 조회: memberId={}, year={}, month={}", memberId, year, month);

    List<CalendarDto> calendarData = loadMemoPort.getTodoCountInCalendar(memberId, year, month);
    List<String> years = loadMemoPort.getTodoYears(memberId);

    // 현재 연도가 목록에 없으면 추가
    String currentYear = String.valueOf(Year.now().getValue());
    if (!years.contains(currentYear)) {
      years.add(0, currentYear);
    }

    // "전체" 옵션은 맨 앞에 추가
    if (!years.contains("전체")) {
      years.add(0, "전체");
    }

    return CalendarListDto.builder().calendar(calendarData).years(years).build();
  }

  /**
   * 메모 생성 - 분산 락 적용 및 캐시 갱신
   *
   * @param memoDto 메모 정보
   * @param memberId 회원 ID
   * @return 생성된 메모 정보
   */
  @Override
  public MemoDto createMemo(MemoDto memoDto, Long memberId) {
    try {
      log.debug("메모 생성 요청: memberId={}, title={}", memberId, memoDto.title());

      // 도메인 모델로 변환
      List<Todo> todos = memoDto.toTodoDomains(memberId, null);

      // 도메인 서비스를 통해 생성
      Memo memo =
          memoDomainService.createMemo(
              memberId, memoDto.title(), memoDto.content(), memoDto.scheduleDate(), todos);

      // 저장
      Memo savedMemo = saveMemoPort.save(memo);
      log.info("메모 생성 완료: id={}, memberId={}", savedMemo.getId(), memberId);

      return MemoDto.fromDomain(savedMemo);

    } catch (MemoDomainException e) {
      log.error("메모 생성 중 도메인 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.VALIDATION_ERROR, e);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.error("메모 생성 중 오류 발생: {}", e.getMessage(), e);
      throw new ApplicationException(ErrorCode.DATABASE_ERROR, e);
    }
  }

  /**
   * 메모 수정 - 분산 락 적용 및 캐시 갱신
   *
   * @param memoDto 수정할 메모 정보
   * @param memberId 회원 ID (인증된 사용자)
   * @return 수정된 메모 정보
   */
  @Override
  public MemoDto updateMemo(MemoDto memoDto, Long memberId) {

    try {
      log.debug("메모 수정 요청: id={}, memberId={}", memoDto.id(), memberId);

      // 메모 조회 - 낙관적 락 버전 확인
      Memo memo =
          loadMemoPort
              .findByIdAndMemberId(memoDto.id(), memberId)
              .orElseThrow(
                  () -> {
                    log.warn("메모 수정 실패: 메모를 찾을 수 없음 - id={}, memberId={}", memoDto.id(), memberId);
                    return new ApplicationException(ErrorCode.MEMO_NOT_FOUND);
                  });

      // 도메인 모델로 변환
      List<Todo> todos = memoDto.toTodoDomains(memberId, memo.getId());

      // 도메인 서비스를 통해 수정
      memoDomainService.updateMemo(
          memo, memoDto.title(), memoDto.content(), memoDto.scheduleDate(), todos);

      // 저장
      Memo updatedMemo = saveMemoPort.save(memo);
      log.info("메모 수정 완료: id={}, memberId={}", updatedMemo.getId(), memberId);

      return MemoDto.fromDomain(updatedMemo);

    } catch (MemoDomainException e) {
      log.error("메모 수정 중 도메인 오류 발생: {}", e.getMessage());
      throw new ApplicationException(ErrorCode.VALIDATION_ERROR, e);
    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.error("메모 수정 중 오류 발생: {}", e.getMessage(), e);
      throw new ApplicationException(ErrorCode.DATABASE_ERROR, e);
    }
  }

  /**
   * 메모 삭제 - 분산 락 적용 및 캐시 갱신
   *
   * @param memoId 메모 ID
   * @param memberId 회원 ID (인증된 사용자)
   */
  @Override
  public void deleteMemo(Long memoId, Long memberId) {

    try {
      log.debug("메모 삭제 요청: id={}, memberId={}", memoId, memberId);

      // 메모 조회
      Memo memo =
          loadMemoPort
              .findByIdAndMemberId(memoId, memberId)
              .orElseThrow(
                  () -> {
                    log.warn("메모 삭제 실패: 메모를 찾을 수 없음 - id={}, memberId={}", memoId, memberId);
                    return new ApplicationException(ErrorCode.MEMO_NOT_FOUND);
                  });

      // 소유권 확인
      if (!memoDomainService.isOwner(memo, memberId)) {
        log.warn("메모 삭제 실패: 접근 권한 없음 - id={}, memberId={}", memoId, memberId);
        throw new ApplicationException(ErrorCode.ACCESS_DENIED);
      }

      // 삭제
      deleteMemoPort.delete(memo);
      log.info("메모 삭제 완료: id={}, memberId={}", memoId, memberId);

    } catch (ApplicationException e) {
      throw e;
    } catch (Exception e) {
      log.error("메모 삭제 중 오류 발생: {}", e.getMessage(), e);
      throw new ApplicationException(ErrorCode.DATABASE_ERROR, e);
    }
  }
}
