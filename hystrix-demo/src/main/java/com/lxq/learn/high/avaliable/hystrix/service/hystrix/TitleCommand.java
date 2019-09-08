package com.lxq.learn.high.avaliable.hystrix.service.hystrix;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxq
 * @create 2019/9/8 21:11
 */
@Slf4j
public class TitleCommand extends HystrixCommand<String> {
    private String id;
    private String serviceUrl;
    private CommonParam commonParam;

    public TitleCommand(String id, String serviceUrl, CommonParam commonParam) {
        super(setter());
        this.id = id;
        this.serviceUrl = serviceUrl;
        this.commonParam = commonParam;
    }

    protected static Setter setter() {
        log.info("create setter");
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");

        return Setter
                .withGroupKey(groupKey)
                ;
    }

    @Override
    protected String run() throws Exception {
        String url = serviceUrl + "?id=" + id;
        if (commonParam != null) {
            if (commonParam.getDelayMilli() != null) {
                url += "&delayMilli=" + commonParam.getDelayMilli();
            }
            if (commonParam.isOccurException()) {
                throw new IllegalArgumentException("customException");
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            return String.valueOf("isInterrupted");
        }
        String result = HttpClientUtils.doGet(url);
        return result;
    }
}
