package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import org.junit.Test;

public class UserCommandTest {


    @Test
    public void test() throws InterruptedException {
        for (int i = 1; i <= 20 ; i++) {
            final String userId = String.valueOf(i);
            new Thread(() -> {
                String result = HttpClientUtils.doGet("http://localhost:8080/user/getById?userId=" + userId);
                System.out.println(result);
            }).start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


}
