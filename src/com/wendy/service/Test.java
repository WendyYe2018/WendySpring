package com.wendy.service;

import com.wendy.service.Aspect.CostTimeAspect;
import com.wendy.service.Aspect.WeakDependsAspect;
import com.wendy.service.Impl.UserInterface;
import com.wendy.spring.SelfApplicationContext;
import com.wendy.spring.aop.Invocation;
import com.wendy.spring.aop.MethodInterceptor;
import com.wendy.spring.aop.ProxyFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        SelfApplicationContext selfApplicationContext = new SelfApplicationContext(AppConfig.class); //this selfconfig has @ComponentScan("com.wendy.service")
        UserInterface userService = (UserInterface)selfApplicationContext.getBean("userService");

        //会变成：invoke(
        //    proxy,
        //    UserInterface.login(),
        //    new Object[]{"wendy"}
        //)
        //进入 InvocationHandler：System.out.println("切面逻辑");
        //((UserInterface)userService).login("wendy");


        userService.test();
    }
}
