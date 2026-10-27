package com.wendy.service.Impl;


import com.wendy.service.annotation.CostTime;
import com.wendy.service.annotation.WeakDepends;

public interface UserInterface {

    @CostTime
    @WeakDepends(retryTimes=4)
    void  test();

    void login(String name);
}
