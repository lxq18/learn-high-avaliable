package com.lxq.learn.high.avaliable.hystrix.service.count;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lxq
 * @create 2019/9/3 22:48
 */
@Slf4j
@Component
public class CountServiceImpl implements CountService {

    @Value("${count.url}")
    private String countUrl;

    @Override
    public Integer getCount(String id, CommonParam commonParam) {
        String url = countUrl + "?id=" + id;
        if (commonParam.getDelayMilli() != null) {
            url += "&delayMilli=" + commonParam.getDelayMilli();
        }
        return Integer.valueOf(HttpClientUtils.doGet(url));
    }

}
