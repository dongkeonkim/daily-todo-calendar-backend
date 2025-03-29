package com.dailytodocalendar.api.member.controller;

import com.dailytodocalendar.api.member.dto.MemberDeleteDto;
import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.api.member.dto.MemberUpdateDto;
import com.dailytodocalendar.api.member.service.MemberService;
import com.dailytodocalendar.common.codes.SuccessCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.config.security.custom.CustomUser;
import jakarta.validation.Valid;
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
    log.debug("회원 정보 조회 요청 - 이메일: {}", customUser.getUsername());
    return ResponseDto.success(
        SuccessCode.SUCCESS, memberService.findMember(customUser.getMemberDto()));
  }

  @PutMapping("/update")
  public ResponseDto<Void> update(
      @AuthenticationPrincipal CustomUser customUser,
      @Valid @RequestBody MemberUpdateDto memberUpdateDto) {

    log.debug("회원 정보 수정 요청 - 이메일: {}", customUser.getUsername());
    memberService.updateMember(customUser.getMemberDto(), memberUpdateDto);
    return ResponseDto.success(SuccessCode.UPDATED);
  }

  @PutMapping("/delete")
  public ResponseDto<Void> delete(@Valid @RequestBody MemberDeleteDto memberDeleteDto) {
    log.debug("회원 탈퇴 요청 - 이메일: {}", memberDeleteDto.getEmail());
    memberService.deleteMember(memberDeleteDto);
    return ResponseDto.success(SuccessCode.DELETED);
  }
}
