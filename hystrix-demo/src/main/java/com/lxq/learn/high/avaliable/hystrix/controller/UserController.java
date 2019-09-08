package com.lxq.learn.high.avaliable.hystrix.controller;

import com.lxq.learn.high.avaliable.hystrix.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/getById")
    public String getNickName(String userId) {
        return userService.getNickName(userId) + "-" + userService.getScore(userId);
    }

}
