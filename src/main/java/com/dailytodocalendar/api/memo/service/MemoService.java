package com.dailytodocalendar.api.memo.service;

import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.memo.dto.CalendarDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import com.dailytodocalendar.api.memo.entity.Memo;
import com.dailytodocalendar.api.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional(readOnly = true)
    public List<MemoDto> selectMemoAll(Integer year, LocalDate date, MemberDto memberDto) {
        return memoRepository.findAllByMemberIdAndDate(memberDto.getId(), year, date);
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getTodoCountInCalendar(Integer year, long memberId) {
        return memoRepository.getTodoCountInCalendar(year, memberId);
    }

    @Transactional(readOnly = true)
    public List<String> getTodoCompleteYears(long memberId) {
        List<String> years = memoRepository.getTodoCompleteYears(memberId);
        String year = String.valueOf(Year.now().getValue());

        if (!years.contains(year)) {
            years.add(year);
        }

        years.add("전체");

        return years;
    }

    @Transactional
    public MemoDto createMemo(MemoDto memoDto, MemberDto memberDto) {
        memoDto.setMemberId(memberDto.getId());
        memoDto.getTodos().forEach(todoDto -> todoDto.setMemberId(memberDto.getId()));

        return memoRepository.save(memoDto.toEntity()).toDto();
    }

    @Transactional
    public MemoDto updateMemo(MemoDto memoDto, MemberDto memberDto) {
        Memo memo = memoRepository.findByIdAndMemberId(memoDto.getId(), memberDto.getId())
                .orElseThrow(IllegalArgumentException::new);

        memoDto.getTodos().forEach(todoDto -> todoDto.setMemberId(memberDto.getId()));
        memo.update(memoDto);
        memoRepository.save(memo);

        return memo.toDto();
    }

    @Transactional
    public void deleteMemo(Long id, MemberDto memberDto) {
        Memo memo = memoRepository.findByIdAndMemberId(id, memberDto.getId())
                .orElseThrow(() -> new EmptyResultDataAccessException(HttpStatus.NOT_FOUND.value()));

        memoRepository.delete(memo);
    }

}
