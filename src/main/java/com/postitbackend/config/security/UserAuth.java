package com.postitbackend.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuth {

    private Long id;
    private String email;
    private String auth;

}
