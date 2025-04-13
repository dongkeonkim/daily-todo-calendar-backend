package com.dailytodocalendar.infrastructure.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "oauth2.kakao")
@Component
@Getter
@Setter
public class KakaoProperties {
  private String clientId;
  private String clientSecret;
  private String redirectUri;
  private String adminKey;
}
