package com.lxq.learn.high.avaliable.hystrix.service.count;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: bjlixiaoqiang
 * @create: 2019-09-04 20:50
 **/
@Slf4j
public class CountServiceCommand extends HystrixCommand<Integer> {
    private String id;
    private String serviceUrl;
    private CommonParam commonParam;

    public CountServiceCommand(String id, String serviceUrl, CommonParam commonParam) {
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
                .withMaxQueueSize(2)
                .withQueueSizeRejectionThreshold(5);
        //命令属性配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                //
                .withFallbackEnabled(true)
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(10)
                .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutEnabled(true)
                .withExecutionTimeoutInMilliseconds(1000)
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
        Integer result = Integer.valueOf(HttpClientUtils.doGet(url));
        log.info("execute success, id = " + id + ", result = " + result);
        return result;
    }

    @Override
    protected Integer getFallback() {
        log.error("execute fallback, id = " + id);
        //返回托底数据或默认值，注意此处尽量不要调用延迟高的服务
        return -1;
    }
}
