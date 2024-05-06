package com.postitbackend.member.controller;

import com.postitbackend.config.security.custom.CustomUser;
import com.postitbackend.member.dto.MemberDTO;
import com.postitbackend.member.service.MemberService;
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
        MemberDTO memberDTO = memberService.findMember(customUser.getMemberDTO());

        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO.toMemberSearchResult(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/create")
    public MemberDTO createMember(@RequestBody MemberDTO memberDTO) {
        return memberService.createMember(memberDTO);
    }

    @Secured("ROLE_USER")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PutMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody MemberDTO memberDTO) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
