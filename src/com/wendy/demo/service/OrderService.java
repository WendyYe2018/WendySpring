package com.wendy.demo.service;

import com.wendy.demo.annotation.CostTime;
import com.wendy.spring.Component;
import com.wendy.spring.InitializingBean;

@Component
public class OrderService{

    @CostTime
    public void buy(String name) {
        System.out.println("OrderService buy method called with name: " + name);
    }
}
