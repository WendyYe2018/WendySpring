package com.wendy.spring.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static Object createProxy(
            Object target,
            List<MethodInterceptor>
                    allInterceptors) {

        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {

                    List<MethodInterceptor> chain =
                            buildChain(method, allInterceptors);

                    if (chain.isEmpty()) {

                        return method.invoke(
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