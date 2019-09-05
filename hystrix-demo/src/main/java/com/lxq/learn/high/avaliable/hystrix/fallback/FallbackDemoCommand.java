package com.lxq.learn.high.avaliable.hystrix.fallback;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import static com.lxq.learn.high.avaliable.hystrix.constant.Constants.*;

/**
 * @author: bjlixiaoqiang
 * @create: 2019-09-04 20:50
 **/
@Slf4j
public class FallbackDemoCommand extends HystrixCommand<Integer> {
    private String id;
    private String serviceUrl;
    private CommonParam commonParam;

    public FallbackDemoCommand(String id, String serviceUrl, CommonParam commonParam) {
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
    private static Setter setter() {
        log.debug("init setter");
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("getCount");
        //线程池名称(服务分组+Single、服务分组+Multi、混合模式)
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("doc-getCount-pool");
        //线程池配置
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(2)
                .withMaximumSize(2)
                .withKeepAliveTimeMinutes(1)
                .withMaxQueueSize(-1)
                .withQueueSizeRejectionThreshold(3);
        //命令属性配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                //
                .withFallbackEnabled(true)
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(10)
                .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutEnabled(true)
                .withExecutionTimeoutInMilliseconds(3000)
                ;

        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected Integer run() throws Exception {
        log.debug("execute start, id = " + id);
        String url = serviceUrl + "?id=" + id;
        if (commonParam.getDelayMilli() != null) {
            url += "&delayMilli=" + commonParam.getDelayMilli();
        }
        if (commonParam.isOccurException()) {
            throw new IllegalArgumentException("customException");
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
            return COUNT_RESULT_EXCEPTION;
        }
        return COUNT_RESULT_DEFAULT;
    }
}
