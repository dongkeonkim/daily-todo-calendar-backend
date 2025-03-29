package com.dailytodocalendar.api.memo.service;

import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.memo.dto.CalendarDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import com.dailytodocalendar.api.memo.entity.Memo;
import com.dailytodocalendar.api.memo.repository.MemoRepository;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

  private final MemoRepository memoRepository;

  @Transactional(readOnly = true)
  public List<MemoDto> selectMemoAll(Integer year, LocalDate date, MemberDto memberDto) {
    log.debug("Fetching memos for member: {}, year: {}, date: {}", memberDto.getId(), year, date);
    return memoRepository.findAllByMemberIdAndDate(memberDto.getId(), year, date);
  }

  @Transactional(readOnly = true)
  @org.springframework.cache.annotation.Cacheable(
      value = "calendarDataCache",
      key = "'calendar_' + #memberId + '_' + #year")
  public List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId) {
    log.debug("Fetching calendar todo counts for year: {}, member: {}", year, memberId);
    return memoRepository.getTodoCountInCalendar(year, memberId);
  }

  @Transactional(readOnly = true)
  @org.springframework.cache.annotation.Cacheable(
      value = "todoYearsCache",
      key = "'years_' + #memberId")
  public List<String> getTodoCompleteYears(long memberId) {
    log.debug("Fetching todo years for member: {}", memberId);
    List<String> years = memoRepository.getTodoCompleteYears(memberId);
    String currentYear = String.valueOf(Year.now().getValue());

    if (!years.contains(currentYear)) {
      years.add(currentYear);
    }

    years.add("전체");
    return years;
  }

  @Transactional
  @org.springframework.cache.annotation.CacheEvict(
      value = {"calendarDataCache", "todoYearsCache"},
      allEntries = true)
  public MemoDto createMemo(MemoDto memoDto, MemberDto memberDto) {
    try {
      memoDto.setMemberId(memberDto.getId());
      if (memoDto.getTodos() != null) {
        memoDto.getTodos().forEach(todoDto -> todoDto.setMemberId(memberDto.getId()));
      }

      Memo savedMemo = memoRepository.save(memoDto.toEntity());
      log.info("Created memo with ID: {} for member: {}", savedMemo.getId(), memberDto.getId());
      return savedMemo.toDto();
    } catch (Exception e) {
      log.error("Error creating memo for member {}: {}", memberDto.getId(), e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }

  @Transactional
  @org.springframework.cache.annotation.CacheEvict(
      value = {"calendarDataCache", "todoYearsCache"},
      allEntries = true)
  public MemoDto updateMemo(MemoDto memoDto, MemberDto memberDto) {
    Memo memo =
        memoRepository
            .findByIdAndMemberId(memoDto.getId(), memberDto.getId())
            .orElseThrow(
                () -> {
                  log.warn(
                      "Memo not found for ID: {} and member: {}",
                      memoDto.getId(),
                      memberDto.getId());
                  return new ApplicationException(ErrorCode.USER_NOT_FOUND);
                });

    try {
      if (memoDto.getTodos() != null && !memoDto.getTodos().isEmpty()) {
        memoDto.getTodos().forEach(todoDto -> todoDto.setMemberId(memberDto.getId()));
      }

      memo.update(memoDto);
      memoRepository.save(memo);

      log.info("Updated memo with ID: {} for member: {}", memo.getId(), memberDto.getId());
      return memo.toDto();
    } catch (Exception e) {
      log.error("Error updating memo {}: {}", memoDto.getId(), e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }

  @Transactional
  @org.springframework.cache.annotation.CacheEvict(
      value = {"calendarDataCache", "todoYearsCache"},
      allEntries = true)
  public void deleteMemo(Long id, MemberDto memberDto) {
    Memo memo =
        memoRepository
            .findByIdAndMemberId(id, memberDto.getId())
            .orElseThrow(
                () -> {
                  log.warn(
                      "Memo not found for deletion. ID: {}, member: {}", id, memberDto.getId());
                  return new ApplicationException(ErrorCode.USER_NOT_FOUND);
                });

    try {
      memoRepository.delete(memo);
      log.info("Deleted memo with ID: {} for member: {}", id, memberDto.getId());
    } catch (Exception e) {
      log.error("Error deleting memo {}: {}", id, e.getMessage());
      throw new ApplicationException(ErrorCode.DATABASE_ERROR);
    }
  }
}
