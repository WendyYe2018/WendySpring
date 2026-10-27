package com.wendy.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //只能卸载类上面
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) //运行时有效
public @interface ComponentScan {

    String value() default ""; // 用于指定扫描路径
}
