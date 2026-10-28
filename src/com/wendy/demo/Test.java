package com.wendy.demo;

import com.wendy.demo.service.Impl.UserInterface;
import com.wendy.demo.service.OrderService;
import com.wendy.demo.service.UserService;
import com.wendy.spring.SelfApplicationContext;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        SelfApplicationContext selfApplicationContext = new SelfApplicationContext(AppConfig.class); //this selfconfig has @ComponentScan("com.wendy.service")
        UserService userInterface = (UserService)selfApplicationContext.getBean("userService");

        OrderService orderService = (OrderService)selfApplicationContext.getBean("orderService");



        orderService.buy("jacket");
        userInterface.test();

        // 关于login方法，不能直接        Method method =
        //                userService..getMethod(
        //                                "login",
        //                                String.class
        //                        );
        // 否则拿到proxy 就不能直接invoke了
        Method method =
                userInterface.getClass()
                        .getMethod(
                                "login",
                                String.class
                        );

        method.invoke(
                userInterface,
                "wendy"
        );


    }
}
