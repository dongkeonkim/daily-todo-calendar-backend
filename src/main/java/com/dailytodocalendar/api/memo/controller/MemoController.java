package com.dailytodocalendar.api.memo.controller;

import com.dailytodocalendar.api.memo.dto.CalendarListDto;
import com.dailytodocalendar.api.memo.dto.MemoDto;
import com.dailytodocalendar.api.memo.service.MemoService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.config.security.custom.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("")
    public ResponseDto<List<MemoDto>> selectMemoAll(@RequestParam(required = false, value = "year") Integer year,
                                                    @RequestParam(required = false, value = "date") LocalDate date,
                                                    @AuthenticationPrincipal CustomUser customUser) {
        return ResponseDto.success(
                SuccessCode.SUCCESS,
                memoService.selectMemoAll(year, date, customUser.getMemberDto())
        );
    }

    @GetMapping("/calendar")
    public ResponseDto<CalendarListDto> getTodoCountInCalendar(@RequestParam(required = false, value = "year") Integer year,
                                                               @AuthenticationPrincipal CustomUser customUser) {
        CalendarListDto calendarListDto = new CalendarListDto();
        calendarListDto.setCalendar(memoService.getTodoCountInCalendar(year, customUser.getMemberDto().getId()));
        calendarListDto.setYears(memoService.getTodoCompleteYears(customUser.getMemberDto().getId()));

        return ResponseDto.success(
                SuccessCode.SUCCESS,
                calendarListDto
        );
    }

    @PostMapping("/create")
    public ResponseDto<MemoDto> createMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        return ResponseDto.success(
                SuccessCode.CREATED,
                memoService.createMemo(memoDto, customUser.getMemberDto())
        );
    }

    @PutMapping("/update")
    public ResponseDto<MemoDto> updateMemo(@RequestBody MemoDto memoDto, @AuthenticationPrincipal CustomUser customUser) {
        return ResponseDto.success(
                SuccessCode.UPDATED,
                memoService.updateMemo(memoDto, customUser.getMemberDto())
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto<Void> deleteMemo(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUser customUser) {
        memoService.deleteMemo(id, customUser.getMemberDto());
        return ResponseDto.success(SuccessCode.DELETED);
    }

}
