package com.lxq.learn.high.avaliable.service.controller;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lxq
 * @create 2019/9/3 21:30
 */
@Slf4j
@RestController
public class CountController {

    @RequestMapping("/doc/count")
    public Integer getCount(String id, CommonParam commonParam) throws InterruptedException {
        if (commonParam.getDelayMilli() != null && commonParam.getDelayMilli() > 0) {
            Thread.sleep(commonParam.getDelayMilli());
        }
        if (db.containsKey(id)) {
            return db.get(id);
        }
        return 1;
    }

    private static final Map<String, Integer> db = new ConcurrentHashMap<>();

    static {
        for (int i = 1; i < 100; i++) {
            db.put(String.valueOf(i), i * 100);
        }
    }
}
