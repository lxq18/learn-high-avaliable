package com.lxq.learn.high.avaliable.hystrix.service;

public interface UserService {

    /**
     * 用户服务-根据用户id获取用户昵称
     * @param userId
     * @return
     */
    String getNickName(String userId);

    /**
     * 积分服务-根据用户id获取积分
     * @return
     */
    int getScore(String userId);

}
