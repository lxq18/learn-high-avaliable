package com.lxq.learn.high.avaliable.hystrix.service.count;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;

public interface CountService {
    Integer getCount(String id, CommonParam commonParam);
}
