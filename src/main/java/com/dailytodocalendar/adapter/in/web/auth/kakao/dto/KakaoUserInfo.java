package com.dailytodocalendar.adapter.in.web.auth.kakao.dto;

import lombok.Builder;

/** 카카오 유저 정보 DTO */
@Builder
public record KakaoUserInfo(Long id, String nickname, String email) {}
