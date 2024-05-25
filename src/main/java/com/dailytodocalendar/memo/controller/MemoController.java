package com.dailytodocalendar.memo.controller;

import com.dailytodocalendar.config.security.custom.CustomUser;
import com.dailytodocalendar.memo.dto.MemoDto;
import com.dailytodocalendar.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/all")
    public ResponseEntity<List<MemoDto>> selectMemoAll(@AuthenticationPrincipal CustomUser customUser) {
        List<MemoDto> memoDtoList = memoService.selectMemoAll(customUser.getMemberDto());

        if (memoDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(memoDtoList);
        }

        return ResponseEntity.status(HttpStatus.OK).body(memoDtoList);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            memoService.createMemo(memoDto, customUser.getMemberDto());
            return ResponseEntity.status(HttpStatus.CREATED).body("생성되었습니다.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String > updateMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            memoService.updateMemo(memoDto, customUser.getMemberDto());
            return ResponseEntity.status(HttpStatus.OK).body("수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메모를 찾지 못했습니다.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            memoService.deleteMemo(memoDto, customUser.getMemberDto());
            return ResponseEntity.status(HttpStatus.OK).body("삭제되었습니다.");
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메모를 찾지 못했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
