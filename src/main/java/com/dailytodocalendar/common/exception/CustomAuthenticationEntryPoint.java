package com.dailytodocalendar.common.exception;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("code", ErrorCode.INVALID_TOKEN.getStatus().value());
    responseBody.put("message", ErrorCode.INVALID_TOKEN.getMessage());
    responseBody.put("path", request.getRequestURI());

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(responseBody);
    response.getWriter().write(jsonResponse);
  }
}
