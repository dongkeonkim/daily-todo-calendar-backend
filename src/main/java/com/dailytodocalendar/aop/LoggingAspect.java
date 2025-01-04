package com.dailytodocalendar.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@within(org.springframework.web.bind.annotation.RestController)")
    public void logControllerParams(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "Anonymous"; // 기본값
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }

        log.info("User: " + username);
        log.info("Class: " + className);
        log.info("Method: " + methodName);
        log.info("Parameters: " + Arrays.toString(args));
    }

}