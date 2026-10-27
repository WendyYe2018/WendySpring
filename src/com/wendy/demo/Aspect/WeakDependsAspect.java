package com.wendy.demo.Aspect;


import com.wendy.spring.Component;
import com.wendy.demo.annotation.WeakDepends;
import com.wendy.spring.aop.Invocation;
import com.wendy.spring.aop.MethodInterceptor;

import java.lang.reflect.Method;

@Component
public class WeakDependsAspect
        implements MethodInterceptor {

    @Override
    public boolean match(Method method) {
        return method.isAnnotationPresent(
                WeakDepends.class
        );
    }

    @Override
    public Object invoke(
            Invocation invocation)
            throws Exception {
        Throwable lastException = null;
        Method method = invocation.getMethod();
        WeakDepends weakDepends =
                method.getAnnotation(WeakDepends.class);
        for (int i = 0; i < weakDepends.retryTimes(); i++) {

            try {

                return invocation.proceed();

            } catch (Throwable e) {
                lastException = e;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        System.out.println(
                "WeakDepends invoke failed after {} retries, method={}" +
                        weakDepends.retryTimes() + lastException
        );

        return weakDepends.defaultValue();
    }
}