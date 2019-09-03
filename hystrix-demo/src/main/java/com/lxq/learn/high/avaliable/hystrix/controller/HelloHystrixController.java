package com.lxq.learn.high.avaliable.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author lxq
 * @create 2019/9/3 21:30
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HelloHystrixController {
    @RequestMapping("/hello")
    public String hello() {
        log.info("hello Hystrix Access");
        return "Hello Hystrix, time = " + new Date();
    }
}
