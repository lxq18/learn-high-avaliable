package com.lxq.learn.high.avaliable.hystrix.controller;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.common.dto.Doc;
import com.lxq.learn.high.avaliable.hystrix.service.DocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxq
 * @create 2019/9/3 21:30
 */
@Slf4j
@RestController
public class DocController {
    @Autowired
    private DocService docService;

    @RequestMapping("/doc/info")
    public Doc getDoc(String id, CommonParam commonParam) {
        return docService.getDoc(id, commonParam);
    }
}
