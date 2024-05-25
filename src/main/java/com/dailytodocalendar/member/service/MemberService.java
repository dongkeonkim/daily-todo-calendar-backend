package com.dailytodocalendar.member.service;

import com.dailytodocalendar.member.dto.MemberDto;
import com.dailytodocalendar.member.dto.MemberUpdateDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {

    void createMember(MemberDto memberDto);

    void login(MemberDto member, HttpServletRequest request);

    MemberDto findMember(MemberDto memberDto);

    boolean updateMember(MemberDto memberDto, MemberUpdateDto memberUpdateDto);

    boolean deleteMember(MemberDto memberDto);

}
