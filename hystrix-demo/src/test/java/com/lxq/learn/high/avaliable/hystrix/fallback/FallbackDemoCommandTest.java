package com.lxq.learn.high.avaliable.hystrix.fallback;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.HystrixApplication;
import com.lxq.learn.high.avaliable.hystrix.ProfileConstants;
import com.lxq.learn.high.avaliable.hystrix.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * @author: bjlixiaoqiang
 * @create: 2019-09-05 13:11
 */
@Slf4j
@Profile(ProfileConstants.S1)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HystrixApplication.class})
public class FallbackDemoCommandTest {

    @Value("${count.url}")
    private String countUrl;

    @Test
    public void normal() {
        CommonParam commonParam = new CommonParam();
        int result = new FallbackDemoCommand("1", countUrl, commonParam).execute();
        assertEquals(100, result);
    }



    @Test
    public void exception() {
        CommonParam commonParam = new CommonParam();
        commonParam.setOccurException(true);
        int result = new FallbackDemoCommand("1", countUrl, commonParam).execute();
        assertEquals(Constants.COUNT_RESULT_EXCEPTION, result);
    }

    @Test
    public void timeout() {
        CommonParam commonParam = new CommonParam();
        commonParam.setDelayMilli(3100);
        int result = new FallbackDemoCommand("1", countUrl, commonParam).execute();
        assertEquals(Constants.COUNT_RESULT_TIMEOUT, result);
    }

    @Test
    public void threadPoolReject() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 1; i <= 5; i++) {
            final int id = i;
            executorService.execute(() -> {
                CommonParam commonParam = new CommonParam();
                if (id <= 2) {
                    commonParam.setDelayMilli(2000);
                }
                int result = new FallbackDemoCommand(String.valueOf(id), countUrl, commonParam).execute();
                if (id <= 4) {
                    assertEquals(id * 100, result);
                    log.info("assert sccuess for id : " + id);
                }
            });
        }
        Thread.sleep(8000);
    }

}