package com.wendy.demo.service.Impl;


import com.wendy.demo.annotation.CostTime;
import com.wendy.demo.annotation.WeakDepends;

public interface UserInterface {

    @CostTime
    @WeakDepends(retryTimes=4)
    void  test();

    void login(String name);
}
