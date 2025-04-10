package com.dailytodocalendar.common.util;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.infrastructure.config.security.custom.CustomUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/** 보안 유틸리티 클래스 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

  /**
   * 현재 인증된 사용자의 ID를 가져옴
   *
   * @return 사용자 ID
   * @throws ApplicationException 인증되지 않았거나 CustomUser가 아닌 경우
   */
  public static Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ApplicationException(ErrorCode.UNAUTHORIZED);
    }

    if (!(authentication.getPrincipal() instanceof CustomUser)) {
      throw new ApplicationException(ErrorCode.UNAUTHORIZED);
    }

    return ((CustomUser) authentication.getPrincipal()).getMemberId();
  }

  /**
   * 현재 인증된 사용자의 이메일을 가져옴
   *
   * @return 사용자 이메일
   * @throws ApplicationException 인증되지 않은 경우
   */
  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ApplicationException(ErrorCode.UNAUTHORIZED);
    }

    return authentication.getName();
  }

  /**
   * 인증된 사용자인지 확인
   *
   * @return 인증된 사용자 여부
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated();
  }
}
