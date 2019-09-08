package com.lxq.learn.high.avaliable.hystrix.demo;

import com.netflix.hystrix.*;

/**
 * 隔离演示
 *
 * @author lxq
 * @create 2019/9/8 16:34
 */
public class UserCommand extends HystrixCommand<String> {

    private String userId;

    public UserCommand(String userId) {
        //设置命令分组，用来统计、报告，未指定ThreadPoolKey时默认的线程池分组划分
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("userCommandGroupKey"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        /*****Execution，控制HystrixCommand.run()的执行******/
                        //隔离策略，默认值Thread, 可选THREAD｜SEMAPHORE。
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        //表示设置是否在执行超时时，中断执行,默认值：true
                        .withExecutionIsolationThreadInterruptOnTimeout(true)
                        //设置调用者执行的超时时间,默认值：1000
                        .withExecutionTimeoutInMilliseconds(1000)
                        //使用SEMAPHORE的隔离策略时，设置最大的并发量，默认值10
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10))
                //指定线程池的划分，相同名称使用同一个线程池
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("userThreadPoolKey"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        /******ThreadPool，线程池配置******/
                        //设置核心线程池大小，默认10
                        .withCoreSize(5)
                        //设置线程池最大值
                        .withMaxQueueSize(5)
                        //设置线程多久没有服务后，需要释放（maximumSize-coreSize ）个线程，默认值1
                        .withKeepAliveTimeMinutes(1))

        );
        this.userId = userId;
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(1000);
        return "testNickName-" + userId;
    }

    @Override
    protected String getFallback() {
        return "defaultNickName-" + userId;
    }

}