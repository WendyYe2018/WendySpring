package com.wendy.service;

import com.wendy.spring.Component;
import com.wendy.spring.InitializingBean;

@Component
public class OrderService implements InitializingBean{

    @Override
    public void afterPropertiesSet() {
        System.out.println("加载订单缓存");
    }

}
