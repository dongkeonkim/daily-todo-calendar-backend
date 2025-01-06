package com.dailytodocalendar.api.member.controller;

import com.dailytodocalendar.api.member.dto.MemberDeleteDto;
import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.member.dto.MemberUpdateDto;
import com.dailytodocalendar.api.member.service.MemberService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.config.security.custom.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseDto<MemberDto> userInfo(@AuthenticationPrincipal CustomUser customUser) {
        return ResponseDto.success(SuccessCode.SUCCESS, memberService.findMember(customUser.getMemberDto())
        );
    }

    @PutMapping("/update")
    public ResponseDto<Void> update(@AuthenticationPrincipal CustomUser customUser, @RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(customUser.getMemberDto(), memberUpdateDto);
        return ResponseDto.success(SuccessCode.UPDATED);
    }

    @PutMapping("/delete")
    public ResponseDto<Void> delete(@RequestBody MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(memberDeleteDto);
        return ResponseDto.success(SuccessCode.DELETED);
    }
}
