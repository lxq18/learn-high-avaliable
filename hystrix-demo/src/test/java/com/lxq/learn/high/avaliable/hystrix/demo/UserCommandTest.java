package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;

public class UserCommandTest {


    public static void main(String[] args) {
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
