package com.dailytodocalendar.adapter.out.persistence.memo;

import com.dailytodocalendar.application.port.in.memo.dto.CalendarDto;
import com.dailytodocalendar.application.port.out.memo.DeleteMemoPort;
import com.dailytodocalendar.application.port.out.memo.LoadMemoPort;
import com.dailytodocalendar.application.port.out.memo.SaveMemoPort;
import com.dailytodocalendar.domain.memo.model.Memo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 메모 영속성 어댑터 - 아웃바운드 포트 구현 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MemoPersistenceAdapter implements LoadMemoPort, SaveMemoPort, DeleteMemoPort {

  private final MemoRepository memoRepository;
  private final MemoMapper memoMapper;

  /**
   * ID로 메모 조회
   *
   * @param id 메모 ID
   * @return 메모 엔티티 옵셔널
   */
  @Override
  public Optional<Memo> findById(Long id) {
    log.debug("메모 조회 by ID: {}", id);
    return memoRepository.findById(id).map(memoMapper::toDomain);
  }

  /**
   * 회원 ID와 메모 ID로 메모 조회
   *
   * @param id 메모 ID
   * @param memberId 회원 ID
   * @return 메모 엔티티 옵셔널
   */
  @Override
  public Optional<Memo> findByIdAndMemberId(Long id, Long memberId) {
    log.debug("메모 조회 by ID와 회원 ID: {}, {}", id, memberId);
    return memoRepository.findByIdAndMemberId(id, memberId).map(memoMapper::toDomain);
  }

  /**
   * 회원의 메모 목록 조회 (필터링 가능)
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @param date 날짜 (선택)
   * @return 메모 엔티티 목록
   */
  @Override
  public List<Memo> findAllByMemberIdAndDate(
      Long memberId, Integer year, Integer month, LocalDate date) {
    log.debug("메모 목록 조회: memberId={}", memberId);

    List<MemoJpaEntity> memos;

    if (date != null) {
      // 날짜가 지정된 경우
      memos = memoRepository.findByMemberIdAndScheduleDateOrderByRegDateDesc(memberId, date);
    } else if (year != null && month != null) {
      // 연도와 월이 지정된 경우
      memos = memoRepository.findByMemberIdAndYearAndMonth(memberId, year, month);
    } else if (year != null) {
      // 연도만 지정된 경우
      memos = memoRepository.findByMemberIdAndYear(memberId, year);
    } else {
      // 필터 없이 전체 조회
      memos = memoRepository.findByMemberIdOrderByScheduleDateDesc(memberId);
    }

    return memos.stream().map(memoMapper::toDomain).collect(Collectors.toList());
  }

  /**
   * 캘린더용 할일 통계 조회
   *
   * @param memberId 회원 ID
   * @param year 연도 (선택)
   * @param month 월 (선택)
   * @return 캘린더 DTO 목록
   */
  @Override
  public List<CalendarDto> getTodoCountInCalendar(Long memberId, Integer year, Integer month) {
    log.debug("캘린더 데이터 조회: memberId={}, year={}, month={}", memberId, year, month);
    return memoRepository.getTodoCountInCalendar(memberId, year, month);
  }

  /**
   * 회원의 할일이 있는 연도 목록 조회
   *
   * @param memberId 회원 ID
   * @return 연도 목록
   */
  @Override
  public List<String> getTodoYears(Long memberId) {
    log.debug("할일 연도 목록 조회: memberId={}", memberId);
    return memoRepository.getTodoYears(memberId);
  }

  /**
   * 메모 저장
   *
   * @param memo 메모 엔티티
   * @return 저장된 메모 엔티티
   */
  @Override
  public Memo save(Memo memo) {
    log.debug("메모 저장: id={}, memberId={}", memo.getId(), memo.getMemberId());
    MemoJpaEntity memoEntity = memoMapper.toJpaEntity(memo);
    MemoJpaEntity savedEntity = memoRepository.save(memoEntity);
    return memoMapper.toDomain(savedEntity);
  }

  /**
   * 메모 삭제
   *
   * @param memo 메모 엔티티
   */
  @Override
  public void delete(Memo memo) {
    log.debug("메모 삭제: id={}, memberId={}", memo.getId(), memo.getMemberId());
    MemoJpaEntity memoEntity = memoMapper.toJpaEntity(memo);
    memoRepository.delete(memoEntity);
  }
}
