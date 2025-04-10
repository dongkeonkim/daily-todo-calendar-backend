package com.dailytodocalendar.adapter.in.web.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/** 웹 어댑터 애너테이션 - 웹 어댑터임을 명시적으로 표현 - RestController를 포함 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@Component
public @interface WebAdapter {

  /** REST API 경로 */
  @AliasFor(annotation = RestController.class)
  String value() default "";
}
