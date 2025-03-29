package com.dailytodocalendar.aop;

import com.dailytodocalendar.common.annotation.RateLimit;
import com.dailytodocalendar.common.codes.ErrorCode;
import com.dailytodocalendar.common.exception.ApplicationException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@Profile("prod")
@RequiredArgsConstructor
public class RateLimitAspect {

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  @Around("@annotation(com.dailytodocalendar.common.annotation.RateLimit)")
  public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    RateLimit rateLimit = method.getAnnotation(RateLimit.class);

    String key = resolveKey(rateLimit.key(), method);

    Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(rateLimit));

    if (bucket.tryConsume(1)) {
      return joinPoint.proceed();
    } else {
      log.warn("Rate limit exceeded for method {} with key {}", method.getName(), key);
      throw new ApplicationException(ErrorCode.SERVER_ERROR);
    }
  }

  private Bucket createBucket(RateLimit rateLimit) {
    long durationInSeconds = convertToSeconds(rateLimit.duration(), rateLimit.unit());
    Bandwidth limit =
        Bandwidth.classic(
            rateLimit.limit(),
            Refill.greedy(rateLimit.limit(), Duration.ofSeconds(durationInSeconds)));

    return Bucket.builder().addLimit(limit).build();
  }

  private String resolveKey(String keyType, Method method) {
    String methodKey = method.getDeclaringClass().getSimpleName() + "." + method.getName();

    switch (keyType) {
      case "ip":
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String ip = getClientIP(request);
        return methodKey + ":" + ip;

      case "principal":
        return methodKey + ":anonymous";

      default:
        return methodKey + ":" + keyType;
    }
  }

  private String getClientIP(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  private long convertToSeconds(long duration, TimeUnit timeUnit) {
    return timeUnit.toSeconds(duration);
  }
}
