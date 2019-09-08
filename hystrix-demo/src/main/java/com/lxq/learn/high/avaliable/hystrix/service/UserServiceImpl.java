package com.lxq.learn.high.avaliable.hystrix.service;

import com.lxq.learn.high.avaliable.hystrix.demo.UserCommand;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

//    /**
//     * hystrix实现
//     * @param userId
//     * @return
//     */
//    @Override
//    public String getNickName(String userId) {
//        UserCommand userCommand = new UserCommand(userId);
//        return userCommand.execute();
//    }


    @Override
    public String getNickName(String userId) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "testNickName-" + userId;
    }

}
