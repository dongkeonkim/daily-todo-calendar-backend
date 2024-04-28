package com.postitbackend.postIt.memo.service;

import com.postitbackend.postIt.memo.dto.MemoDTO;
import com.postitbackend.postIt.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public List<MemoDTO> findAllByEmail(String email) {
//        List<Memo> results = memoRepository.findAllByEmail(email);
        List<MemoDTO> memoDTOs = new ArrayList<>();
//
//        for (Memo memo : results) {
//            memoDTOs.add(memo.toDTO());
//        }
//
        return memoDTOs;
    }

    @Transactional
    public List<MemoDTO> findFixedMemos(String email) {
//        List<Memo> results = memoRepository.findFixedMemos(email);
        List<MemoDTO> memoDTOs = new ArrayList<>();

//        for (Memo memo : results) {
//            memoDTOs.add(memo.toDTO());
//        }

        return memoDTOs;
    }

    @Transactional
    public MemoDTO findOne() {
        return new MemoDTO();
    }

    @Transactional
    public int save(MemoDTO memoDTO) {
        try {
            memoRepository.save(memoDTO.toEntity());
            return 1;
        } catch (DataAccessException e) {
            log.warn("Fail to insert memo: {}", e.getMessage());
            return 0;
        }
    }

}
