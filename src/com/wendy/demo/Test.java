package com.wendy.demo;

import com.wendy.demo.service.Impl.UserInterface;
import com.wendy.spring.SelfApplicationContext;

import java.lang.reflect.Method;

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

        Method method =
                UserService.class
                        .getMethod("sayHello");

        Object result =
                method.invoke(userService);

        System.out.println(result);

    }
}
