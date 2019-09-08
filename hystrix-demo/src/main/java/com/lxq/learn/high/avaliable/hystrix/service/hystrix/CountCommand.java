package com.lxq.learn.high.avaliable.hystrix.service.hystrix;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import static com.lxq.learn.high.avaliable.hystrix.constant.Constants.COUNT_RESULT_INTERRUPTED;

/**
 * @author lxq
 * @create 2019/9/8 21:11
 */
@Slf4j
public class CountCommand extends HystrixCommand<Integer> {
    private String id;
    private String serviceUrl;
    private CommonParam commonParam;

    public CountCommand(String id, String serviceUrl, CommonParam commonParam) {
        super(setter());
        this.id = id;
        this.serviceUrl = serviceUrl;
        this.commonParam = commonParam;
    }

    protected static Setter setter() {
        log.info("create setter");
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");

        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                ;
    }

    @Override
    protected Integer run() throws Exception {
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
            return COUNT_RESULT_INTERRUPTED;
        }
        Integer result = Integer.valueOf(HttpClientUtils.doGet(url));
        return result;
    }
}
