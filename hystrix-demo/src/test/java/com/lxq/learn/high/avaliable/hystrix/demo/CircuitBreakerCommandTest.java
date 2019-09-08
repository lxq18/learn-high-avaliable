package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.HystrixApplication;
import com.lxq.learn.high.avaliable.hystrix.constant.ProfileConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@Slf4j
@Profile(ProfileConstants.S1)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HystrixApplication.class})
public class CircuitBreakerCommandTest {
    @Value("${count.url}")
    private String countUrl;

    /**
     * 先1个超时
     * 再1个异常
     * 然后正常请求6个
     * 然后第10个还正常
     */
    @Test
    public void notBreaker() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        int taskSize = 9;
        for (int i = 1; i <= taskSize; i++) {
            final int id = i;
            executorService.execute(() -> {
                CommonParam commonParam = new CommonParam();
                if (id == 1) {
                    commonParam.setDelayMilli(1005);
                } else if (id == 2) {
                    commonParam.setOccurException(true);
                }
                int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, commonParam).execute();
                if (id <= taskSize) {
                    assertEquals(id * 100, result);
                    log.info("assert sccuess for id : " + id);
                }
            });
        }

        Thread.sleep(2000);

        int id = 30;
        int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, null).execute();
        assertEquals(id * 100, result);
        log.info("assert sccuess for id : " + id);
    }

    /**
     * 先2个超时
     * 再1个异常
     * 然后正常请求6个
     * 然后第10个还正常
     */
    @Test
    public void justNotBreaker() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        int taskSize = 9;
        for (int i = 1; i <= taskSize; i++) {
            final int id = i;
            executorService.execute(() -> {
                CommonParam commonParam = new CommonParam();
                if (id <= 2) {
                    commonParam.setDelayMilli(1005);
                } else if (id == 3) {
                    commonParam.setOccurException(true);
                }
                int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, commonParam).execute();
                if (id <= taskSize) {
                    assertEquals(id * 100, result);
                    log.info("assert sccuess for id : " + id);
                }
            });
        }

        Thread.sleep(2000);

        int id = 30;
        int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, null).execute();
        assertEquals(id * 100, result);
        log.info("assert sccuess for id : " + id);
    }

    /**
     * 先2个超时
     * 再1个异常
     * 然后正常请求7个
     * 然后第10个异常了
     */
    @Test
    public void breaker() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        int taskSize = 10;
        for (int i = 1; i <= taskSize; i++) {
            final int id = i;
            executorService.execute(() -> {
                CommonParam commonParam = new CommonParam();
                if (id <= 2) {
                    commonParam.setDelayMilli(1005);
                } else if (id == 3) {
                    commonParam.setOccurException(true);
                }
                int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, commonParam).execute();
                if (id <= taskSize) {
                    assertEquals(id * 100, result);
                    log.info("assert sccuess for id : " + id);
                }
            });
        }

        Thread.sleep(2000);

        int id = 30;
        int result = new CircuitBreakerCommand(String.valueOf(id), countUrl, null).execute();
        assertEquals(id * 100, result);
        log.info("assert sccuess for id : " + id);
    }
}