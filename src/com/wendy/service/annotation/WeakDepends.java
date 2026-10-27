package com.wendy.service.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WeakDepends {

    int retryTimes() default 3;

    String defaultValue() default "";
}