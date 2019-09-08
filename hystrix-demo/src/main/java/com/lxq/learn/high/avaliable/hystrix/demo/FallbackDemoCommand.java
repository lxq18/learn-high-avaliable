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
    protected static Setter setter() {
        log.info("create setter");
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("getCount");
        //线程池名称(服务分组+Single、服务分组+Multi、混合模式)
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("doc-getCount-pool");
        //线程池配置
        /*
        在实践中，这通常是这样的：

        1、保留默认的1000ms超时，除非知道需要更多时间。
        2、将线程池保留为默认的10个线程，除非知道需要更多线程。
        3、部署; 如果一切顺利，继续。
        4、在生产中跑24小时。
        5、依靠警报和监控来捕捉问题（如果有的话）。
        6、24小时后，使用延迟百分位数和流量（线程数：99%请求的qps*延迟ms + 冗余数， 30qps*0.2s + 4 = 10）来计算有意义的最低配置值。
        7、在生产中即时更改值并使用实时仪表板监控它们，直到您确信为止。
        */
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(2)
                .withMaximumSize(3)
                //注意此参数设置了，maximumSize才有效
                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                .withKeepAliveTimeMinutes(1)
                .withMaxQueueSize(-1)
                // TODO 没测通 queueSizeRejectionThreshold 参数有效性
                //.withQueueSizeRejectionThreshold(2)
                ;
        //命令属性配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                .withFallbackEnabled(true)
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(10)
                //cancel只不过是调用了Thread的interrupt方法, interrupt只能是停掉线程中有sleep,wait,join逻辑的线程
                //有一个妥协的做法就是在判断条件中加!Thread.currentThread().isInterrupted()这个判断即可
                .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutEnabled(true)
                .withExecutionTimeoutInMilliseconds(3000);

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
            return COUNT_RESULT_EXCEPTION;
        }
        return COUNT_RESULT_DEFAULT;
    }
}
