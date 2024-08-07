package com.dailytodocalendar.memo.controller;

import com.dailytodocalendar.common.ResponseDto;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<List<MemoDto>>> selectMemoAll(
            @RequestParam(required = false, value = "year") Integer year,
            @RequestParam(required = false, value = "date") LocalDate date,
            @AuthenticationPrincipal CustomUser customUser) {
        ResponseDto<List<MemoDto>> response = new ResponseDto<>();

        List<MemoDto> memoDtoList = memoService.selectMemoAll(year, date, customUser.getMemberDto());

        if (memoDtoList.isEmpty()) {
            response.setMessage("메모가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setData(memoDtoList);
        response.setMessage("조회되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/calendar")
    public ResponseDto<Map<String, Object>> getTodoCountInCalendar(@RequestParam(required = false, value = "year") Integer year, @AuthenticationPrincipal CustomUser customUser) {
        ResponseDto<Map<String, Object>> response = new ResponseDto<>();
        Map<String, Object> result = new HashMap<>();
        result.put("calendar", memoService.getTodoCountInCalendar(year, customUser.getMemberDto().getId()));
        result.put("years", memoService.getTodoCompleteYears(customUser.getMemberDto().getId()));

        response.setData(result);
        response.setMessage("조회되었습니다.");
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<MemoDto>> createMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        ResponseDto<MemoDto> response = new ResponseDto<>();

        try {
            response.setData(memoService.createMemo(memoDto, customUser.getMemberDto()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            response.setMessage("잘못된 요청입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setMessage("서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<MemoDto>> updateMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        ResponseDto<MemoDto> response = new ResponseDto<>();

        try {
            response.setData(memoService.updateMemo(memoDto, customUser.getMemberDto()));
            response.setMessage("수정되었습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            response.setMessage("메모를 찾지 못했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            response.setMessage("잘못된 요청입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto<String>> deleteMemo(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUser customUser) {
        ResponseDto<String> response = new ResponseDto<>();

        try {
            memoService.deleteMemo(id, customUser.getMemberDto());
            response.setMessage("삭제되었습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (EmptyResultDataAccessException ex) {
            response.setMessage("메모를 찾지 못했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.setMessage("서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
