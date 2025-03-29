package com.dailytodocalendar.common.util;

import com.dailytodocalendar.api.member.dto.MemberDto;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.config.security.custom.CustomUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

  public static String getCurrentUserEmail() {
    return getCurrentUser().getEmail();
  }

  public static Long getCurrentUserId() {
    return getCurrentUser().getId();
  }

  public static MemberDto getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal().equals("anonymousUser")) {
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof CustomUser)) {
      throw new ApplicationException(ErrorCode.INVALID_TOKEN);
    }

    return ((CustomUser) principal).getMemberDto();
  }

  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !authentication.getPrincipal().equals("anonymousUser");
  }

  public static boolean hasRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
  }
}
