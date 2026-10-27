package com.wendy.demo.service;

import com.wendy.demo.service.Impl.UserInterface;
import com.wendy.demo.annotation.CostTime;
import com.wendy.demo.annotation.WeakDepends;
import com.wendy.spring.*;

@Component("userService")
public class UserService implements BeanNameAware, UserInterface {
    @Autowired
    // 这个字段名称很重要 必须是OrderService 因为在OrderService类上没有指定bean的名称 所以默认是类名首字母小写写
    private OrderService orderService;

    private String beanName;

    private String property;


    @CostTime
    @WeakDepends(retryTimes = 4)
    @Override
    public void test() {
        throw new RuntimeException("模拟异常");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        System.out.println("Bean name set to: " + beanName);
    }

    @Override
    public void login(String name) {
        System.out.println("UserService login method called with name: " + name);
    }
}
