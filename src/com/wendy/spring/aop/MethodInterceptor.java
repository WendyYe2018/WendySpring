package com.wendy.spring.aop;

import java.lang.reflect.Method;

public interface MethodInterceptor {


    boolean match(Method method);

    Object invoke(Invocation invocation)
            throws Exception;
}
