package com.postitbackend.member.service;

import com.postitbackend.member.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {

    MemberDTO createMember(MemberDTO memberDTO);

    void login(MemberDTO member, HttpServletRequest request);

    MemberDTO findMember(MemberDTO dto);

}
