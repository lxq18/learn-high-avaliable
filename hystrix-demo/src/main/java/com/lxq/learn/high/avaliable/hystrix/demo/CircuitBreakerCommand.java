package com.lxq.learn.high.avaliable.hystrix.demo;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import static com.lxq.learn.high.avaliable.hystrix.constant.Constants.*;

/**
 * 降级演示
 *
 * @author lxq
 * @create 2019/9/8 17:14
 */
@Slf4j
public class CircuitBreakerCommand extends HystrixCommand<Integer> {
    private String id;
    private String serviceUrl;
    private CommonParam commonParam;

    public CircuitBreakerCommand(String id, String serviceUrl, CommonParam commonParam) {
        super(setter());
        this.id = id;
        this.serviceUrl = serviceUrl;
        this.commonParam = commonParam;
    }

    /**
     * 注意静态方法（构造方法使用super()）
     *
     * @return
     */
    protected static Setter setter() {
        log.debug("create setter");
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");
        //命令属性配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withCircuitBreakerEnabled(true)
                .withCircuitBreakerForceClosed(false)
                .withCircuitBreakerForceOpen(false)
                .withCircuitBreakerErrorThresholdPercentage(30) //默认50
                .withCircuitBreakerRequestVolumeThreshold(10) //默认20
                .withCircuitBreakerSleepWindowInMilliseconds(5000) //默认5s
                ;
        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected Integer run() throws Exception {
        log.debug("execute start, id = " + id);
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

    @Override
    protected Integer getFallback() {
        log.error("execute fallback for id : " + id);
        //返回托底数据或默认值，注意此处尽量不要调用延迟高的服务
        if (this.isResponseTimedOut()) {
            return COUNT_RESULT_TIMEOUT;
        }
        if (this.isFailedExecution()) {
            log.error("error, ", this.getFailedExecutionException());
            return COUNT_RESULT_EXCEPTION;
        }
        return COUNT_RESULT_DEFAULT;
    }
}
