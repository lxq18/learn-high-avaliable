package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import org.junit.Test;

public class UserCommandTest {


    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 20 ; i++) {
            new Thread(() -> {
                String result = HttpClientUtils.doGet("http://localhost:8080/user/getById?userId=1");
                System.out.println(result);
            }).start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


}
