package com.wendy.spring.aop;


import java.lang.reflect.Method;
import java.util.List;

public class Invocation {

    private Object target;

    private Method method;

    private Object[] args;

    private List<MethodInterceptor> interceptors;

    private int index = 0;

    private int depth = 0;

    public Invocation(
            Object target,
            Method method,
            Object[] args,
            List<MethodInterceptor> interceptors) {

        this.target = target;
        this.method = method;
        this.args = args;
        this.interceptors = interceptors;
    }

    public Object proceed() throws Exception {

        String prefix = getPrefix(depth);

        if (index >= interceptors.size()) {

            System.out.println(
                    prefix + "└─ Target Method"
            );

            Object result =
                    method.invoke(target, args);

            System.out.println(
                    prefix + "   return -> " + result
            );

            return result;
        }

        MethodInterceptor interceptor =
                interceptors.get(index++);

        System.out.println(
                prefix + "└─ "
                        + interceptor.getClass().getSimpleName()
        );

        depth++;

        Object result =
                interceptor.invoke(this);

        depth--;

        System.out.println(
                prefix + "← "
                        + interceptor.getClass().getSimpleName()
        );

        return result;
    }

    private String getPrefix(int depth) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < depth; i++) {
            sb.append("   ");
        }

        return sb.toString();
    }
    public Method getMethod() {
        return method;
    }
}