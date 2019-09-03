package com.lxq.learn.high.avaliable.sentinel.controller;

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
@RequestMapping("/sentinel")
public class HelloSentinelController {
    @RequestMapping("/hello")
    public String hello() {
        log.info("Hello Sentinel Access");
        return "Hello Sentinel, time = " + new Date();
    }
}
