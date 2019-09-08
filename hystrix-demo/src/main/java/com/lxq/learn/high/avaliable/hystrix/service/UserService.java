package com.lxq.learn.high.avaliable.hystrix.service;

public interface UserService {

    /**
     * 根据用户id获取用户昵称
     * @param userId
     * @return
     */
    String getNickName(String userId);

}
