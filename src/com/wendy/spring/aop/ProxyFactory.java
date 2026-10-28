package com.wendy.spring.aop;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static Object createProxy(
            Object target,
            List<MethodInterceptor> allInterceptors
    ) {

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(
                target.getClass()
        );

        enhancer.setCallback(
                (net.sf.cglib.proxy.MethodInterceptor) (
                        obj,
                        method,
                        args,
                        methodProxy
                ) -> {

                    List<MethodInterceptor> chain =
                            buildChain(
                                    method,
                                    allInterceptors
                            );

                    if (chain.isEmpty()) {

                        return methodProxy.invoke(
                                target,
                                args
                        );
                    }

                    Invocation invocation =
                            new Invocation(
                                    target,
                                    method,
                                    args,
                                    chain
                            );

                    return invocation.proceed();
                }
        );

        return enhancer.create();
    }

    private static List<MethodInterceptor>
    buildChain(
            Method method,
            List<MethodInterceptor>
                    allInterceptors) {
        List<MethodInterceptor> chain =
                new ArrayList<>();

        for(MethodInterceptor interceptor
                : allInterceptors){

            if(interceptor.match(method)){

                chain.add(
                        interceptor
                );
            }
        }

        return chain;
    }
}