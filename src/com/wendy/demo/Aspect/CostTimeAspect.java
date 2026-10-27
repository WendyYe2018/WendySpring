package com.wendy.demo.Aspect;


import com.wendy.spring.Component;
import com.wendy.demo.annotation.CostTime;
import com.wendy.spring.aop.Invocation;
import com.wendy.spring.aop.MethodInterceptor;

import java.lang.reflect.Method;

@Component
public class CostTimeAspect
        implements MethodInterceptor {

    @Override
    public boolean match(Method method) {
        return method.isAnnotationPresent(
                CostTime.class
        );
    }

    @Override
    public Object invoke(Invocation invocation)
            throws Exception {

        long start = System.currentTimeMillis();

        try {
            return invocation.proceed();
        } finally {

            System.out.println(
                    "cost="
                            +(System.currentTimeMillis()-start)
            );
        }
    }
}