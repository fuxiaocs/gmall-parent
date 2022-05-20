package com.atguigu.gmall.common.annotation;


import com.atguigu.gmall.common.config.AppMinIOConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(AppMinIOConfiguration.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableMinIO {
}
