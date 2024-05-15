package com.postitbackend.postIt.memo.service;

import com.postitbackend.postIt.memo.dto.MemoDto;
import com.postitbackend.postIt.memo.entity.Memo;
import com.postitbackend.postIt.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional(readOnly = true)
    public List<MemoDto> selectMemoAllByMemberId(long memberId) {
        return memoRepository.findAllByMemberId(memberId).stream()
                .map(Memo::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemoDto selectMemo(MemoDto memoDto, long memberId) {
        return memoRepository.findById(memoDto.getId())
                .filter(memo -> memo.getMemberId() == memberId)
                .map(Memo::toDto)
                .orElse(null);
    }

    @Transactional
    public boolean createMemo(MemoDto memoDto, long memberId) {
        memoDto.setMemberId(memberId);
        memoDto.setRegDate(LocalDateTime.now());
        memoDto.setUdtDate(LocalDateTime.now());

        try {
            memoRepository.save(memoDto.toEntity());
            log.info("Memo Create Success");
        } catch (DataAccessException e) {
            log.error("Memo Create Fail", e);
            return false;
        }

        return true;
    }

    @Transactional
    public boolean updateMemo(MemoDto memoDto, long memberId) {
        MemoDto mDto = selectMemo(memoDto, memberId);

        if (mDto == null) {
            return false;
        }

        mDto.setTitle(memoDto.getTitle());
        mDto.setUdtDate(LocalDateTime.now());

        memoRepository.save(mDto.toEntity());
        log.info("Memo Update Success");

        return true;
    }

    @Transactional
    public boolean deleteMemo(MemoDto memoDto, long memberId) {
        MemoDto mDto = selectMemo(memoDto, memberId);

        if (mDto == null) {
            return false;
        }

        memoRepository.delete(mDto.toEntity());
        log.info("Memo Update Success");

        return true;
    }

}
