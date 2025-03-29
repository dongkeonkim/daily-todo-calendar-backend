package com.dailytodocalendar.config.filter;

import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.response.ResponseDto;
import com.dailytodocalendar.config.RateLimiterConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
@Profile("prod")
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterConfig rateLimiterConfig;
    private final ObjectMapper objectMapper;
    private final RateLimiterConfig.BucketCleanupJob bucketCleanupJob;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/actuator") || requestURI.startsWith("/management")) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientId = getClientIP(request);

        Bucket bucket = rateLimiterConfig.resolveBucket(clientId);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        bucketCleanupJob.recordAccess(clientId);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            response.addHeader("X-Rate-Limit-Reset", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));

            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for client IP: {}", clientId);

            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ResponseDto<Void> errorResponse = ResponseDto.error(ErrorCode.SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}