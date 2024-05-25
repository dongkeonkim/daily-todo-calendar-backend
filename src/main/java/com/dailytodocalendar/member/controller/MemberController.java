package com.dailytodocalendar.member.controller;

import com.dailytodocalendar.config.security.custom.CustomUser;
import com.dailytodocalendar.member.dto.MemberDto;
import com.dailytodocalendar.member.dto.MemberUpdateDto;
import com.dailytodocalendar.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Secured("ROLE_USER")
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUser customUser) {
        MemberDto memberDto = memberService.findMember(customUser.getMemberDto());

        if (memberDto != null) {
            return new ResponseEntity<>(memberDto.toMemberSearchResult(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMember(@RequestBody MemberDto memberDto) {
        try {
            memberService.createMember(memberDto);
            return ResponseEntity.status(HttpStatus.OK).body("계정이 생성되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일 입니다.");
        }
    }

    @Secured("ROLE_USER")
    @PutMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUser customUser, @RequestBody MemberUpdateDto memberUpdateDto) {
        boolean result = memberService.updateMember(customUser.getMemberDto(), memberUpdateDto);

        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("기존 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

    }

    @Secured("ROLE_USER")
    @PutMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody MemberDto memberDto) {
        boolean result  = memberService.deleteMember(memberDto);

        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

    }
}
