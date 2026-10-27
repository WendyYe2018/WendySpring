package com.wendy.demo;

import com.wendy.spring.BeanPostProcessor;
import com.wendy.spring.Component;
import com.wendy.spring.SelfApplicationContext;
import com.wendy.spring.aop.MethodInterceptor;
import com.wendy.spring.aop.ProxyFactory;

import java.lang.reflect.Method;
import java.util.List;

@Component
public class AopBeanPostProcessor
        implements BeanPostProcessor {

    private List<MethodInterceptor>
            allInterceptors;

    public AopBeanPostProcessor(){

        this.allInterceptors = SelfApplicationContext.getMethodInterceptors();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) {

        for(Method method :
                bean.getClass().getMethods()) {

            for(MethodInterceptor interceptor
                    : allInterceptors){

                if(interceptor.match(method)){

                    System.out.println(
                            "创建代理:"
                                    + beanName
                    );

                    return ProxyFactory
                            .createProxy(
                                    bean,
                                    allInterceptors
                            );
                }
            }
        }

        return bean;
    }
}