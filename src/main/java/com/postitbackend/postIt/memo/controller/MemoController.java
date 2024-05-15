package com.postitbackend.postIt.memo.controller;

import com.postitbackend.config.security.custom.CustomUser;
import com.postitbackend.postIt.memo.dto.MemoDto;
import com.postitbackend.postIt.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/select")
    public ResponseEntity<?> selectMemo(@AuthenticationPrincipal CustomUser customUser) {
        List<MemoDto> memoDtoList = memoService.selectMemoAllByMemberId(customUser.getMemberDTO().getId());

        if (memoDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("memoList", memoDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        boolean result = memoService.createMemo(memoDto, customUser.getMemberDTO().getId());

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            boolean result = memoService.updateMemo(memoDto, customUser.getMemberDTO().getId());

            if (!result) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (DataAccessException e) {
            log.error("Memo Update Fail", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        boolean result  = memoService.deleteMemo(memoDto, customUser.getMemberDTO().getId());

        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
