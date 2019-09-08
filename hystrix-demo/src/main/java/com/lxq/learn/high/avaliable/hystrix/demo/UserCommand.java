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
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                //指定线程池的划分，相同名称使用同一个线程池
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("userThreadPoolKey"))

        );
        this.userId = userId;
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(500);
        return "testNickName-" + userId;
    }

    @Override
    protected String getFallback() {
        return "defaultNickName-" + userId;
    }

}
