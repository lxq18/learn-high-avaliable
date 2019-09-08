package com.lxq.learn.high.avaliable.hystrix.demo;

import org.junit.Test;

import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;

public class DashboardTest {
    @Test
    public void contextLoads() {
        while(true){
            String result = HttpClientUtils.doGet("http://localhost:8080/doc/info?id=3&delayMilli=350");
            System.out.println(result);
        }
    }
}
