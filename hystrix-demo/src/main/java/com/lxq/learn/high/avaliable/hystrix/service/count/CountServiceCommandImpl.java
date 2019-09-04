package com.lxq.learn.high.avaliable.hystrix.service.count;

import com.netflix.hystrix.*;

/**
 * @author: bjlixiaoqiang
 * @create: 2019-09-04 20:50
 **/
public class CountServiceCommandImpl extends HystrixCommand<Integer> {
    private Integer id;

    public CountServiceCommandImpl(Integer id) {
        super(setter());
        this.id = id;
    }

    private static Setter setter() {
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("doc");
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("getCount");
        //线程池名称
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("doc-count-pool");
        //线程池配置
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(10)
                .withKeepAliveTimeMinutes(5)
                .withMaxQueueSize(20)
                .with
        return null;
    }

    @Override
    protected Integer run() throws Exception {
        return null;
    }
}
