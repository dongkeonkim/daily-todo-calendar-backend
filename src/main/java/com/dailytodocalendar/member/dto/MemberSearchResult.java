package com.dailytodocalendar.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberSearchResult {

    private Long id;
    private String email;
    private String name;
    private String role;

}
