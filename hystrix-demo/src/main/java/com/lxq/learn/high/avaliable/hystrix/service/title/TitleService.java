package com.lxq.learn.high.avaliable.hystrix.service.title;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;

/**
 * @author lxq
 * @create 2019/9/3 22:37
 */
public interface TitleService {
    String getTitle(String id, CommonParam commonParam);
}
