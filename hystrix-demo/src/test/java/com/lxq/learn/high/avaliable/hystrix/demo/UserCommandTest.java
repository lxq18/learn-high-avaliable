package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.hystrix.HystrixApplication;
import com.lxq.learn.high.avaliable.hystrix.constant.ProfileConstants;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

public class UserCommandTest {


    public static void main(String[] args) {
//        for (int i = 0; i< 50; i++) {
//            final String userId = String.valueOf(i);
//            new Thread(() -> {
//                UserCommand userCommand = new UserCommand(userId);
//                System.out.println(userCommand.execute());
//            }).start();
//        }

        for (int i = 1; i <= 50; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final String userId = String.valueOf(i);
            new Thread(() -> {
                String result = HttpClientUtils.doGet("http://localhost:8080/user/getNickName?userId=" + userId);
                System.out.println(result);
            }).start();
        }

    }


}
