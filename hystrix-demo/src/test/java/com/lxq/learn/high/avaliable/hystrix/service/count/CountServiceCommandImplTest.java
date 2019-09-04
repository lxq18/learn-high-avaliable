package com.lxq.learn.high.avaliable.hystrix.service.count;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.HystrixApplication;
import com.lxq.learn.high.avaliable.hystrix.ProfileConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@Profile(ProfileConstants.S1)
@RunWith(SpringRunner.class)
@SpringBootTest(classes={HystrixApplication.class})
public class CountServiceCommandImplTest {
    @Value("${count.url}")
    private String countUrl;

    @Test
    public void getFallback() throws Exception {
        CommonParam commonParam = new CommonParam();
        commonParam.setDelayMilli(1200);
        int result = new CountServiceCommand("1", countUrl, commonParam).execute();
        int result2 = new CountServiceCommand("2", countUrl, commonParam).execute();
        Thread.sleep(10000);
        //assertEquals(100, result);
        assertEquals(200, result2);
    }

    @Test
    public void testFallback() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> {
                CommonParam commonParam = new CommonParam();
                commonParam.setDelayMilli(100);
                int result = new CountServiceCommand("1", countUrl, commonParam).execute();
                assertEquals(100, result);
            });
        }
        Thread.sleep(100000);
    }

}