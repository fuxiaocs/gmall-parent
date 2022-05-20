package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.exception.GmallGlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(GmallGlobalExceptionHandler.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableGmallGlobbalExceptionHandler {
}
