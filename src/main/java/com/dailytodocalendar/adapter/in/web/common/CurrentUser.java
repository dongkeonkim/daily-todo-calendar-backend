package com.dailytodocalendar.adapter.in.web.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 현재 로그인한 사용자를 주입받기 위한 애너테이션 - 컨트롤러 메서드 파라미터에 사용 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {}
