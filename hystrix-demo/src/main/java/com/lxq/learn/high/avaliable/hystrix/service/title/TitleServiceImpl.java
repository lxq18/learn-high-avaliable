package com.lxq.learn.high.avaliable.hystrix.service.title;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.hystrix.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TitleServiceImpl implements TitleService {
    @Value("${title.url}")
    private String titleUrl;

    @Override
    public String getTitle(String id, CommonParam commonParam) {
        String url = titleUrl + "?id=" + id;
        if (commonParam.getDelayMilli() != null) {
            url += "&delayMilli=" + commonParam.getDelayMilli();
        }
        return HttpClientUtils.doGet(url);
    }
}
