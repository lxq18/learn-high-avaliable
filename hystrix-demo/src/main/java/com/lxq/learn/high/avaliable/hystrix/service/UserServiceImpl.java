package com.lxq.learn.high.avaliable.hystrix.service;

import com.lxq.learn.high.avaliable.hystrix.demo.UserCommand;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

//    /**
//     * 用户服务-根据用户id获取用户昵称( hystrix实现)
//     * @param userId
//     * @return
//     */
//    @Override
//    public String getNickName(String userId) {
//        UserCommand userCommand = new UserCommand(userId);
//        return userCommand.execute();
//    }


    /**
     * 用户服务-根据用户id获取用户昵称
     * @param userId
     * @return
     */
    @Override
    public String getNickName(String userId) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "testNickName-" + userId;
    }

    /**
     * 积分服务-根据用户id获取积分
     * @return
     */
    @Override
    public int getScore(String userId) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 100;
    }

}
