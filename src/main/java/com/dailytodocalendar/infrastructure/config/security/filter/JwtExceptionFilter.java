package com.dailytodocalendar.infrastructure.config.security.filter;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import com.dailytodocalendar.common.exception.GlobalControllerAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

/** JWT 예외 처리 필터 JwtAuthenticationFilter에서 발생한 예외를 처리하는 필터 */
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final GlobalControllerAdvice globalControllerAdvice;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (ApplicationException e) {
      log.error("JWT 인증 필터 처리 중 애플리케이션 예외: {}", e.getMessage());
      setErrorResponse(response, e);
    } catch (Exception e) {
      log.error("JWT 인증 필터 처리 중 예외 발생: {}", e.getMessage());
      setErrorResponse(response, new ApplicationException(ErrorCode.UNAUTHORIZED));
    }
  }

  /**
   * 에러 응답 설정
   *
   * @param response HTTP 응답
   * @param exception 발생한 예외
   */
  private void setErrorResponse(HttpServletResponse response, ApplicationException exception) {
    response.setStatus(exception.getErrorCode().getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ResponseEntity<?> errorResponse = globalControllerAdvice.handleApplicationException(exception);

    try {
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    } catch (IOException e) {
      log.error("에러 응답 작성 중 오류: {}", e.getMessage());
    }
  }
}
