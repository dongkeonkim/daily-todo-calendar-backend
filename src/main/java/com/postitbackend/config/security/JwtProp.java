package com.postitbackend.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProp {

    @Value("${secret-key}")
    private String secretKey;

}
