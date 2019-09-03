package com.lxq.learn.high.avaliable.hystrix.service;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.common.dto.Doc;

public interface DocService {
    Doc getDoc(String id, CommonParam commonParam);
}
