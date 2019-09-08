package com.lxq.learn.high.avaliable.hystrix.service;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.common.dto.Doc;
import com.lxq.learn.high.avaliable.hystrix.service.hystrix.CountCommand;
import com.lxq.learn.high.avaliable.hystrix.service.hystrix.TitleCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lxq
 * @create 2019/9/3 22:43
 */
@Service
public class DocServiceImpl implements DocService {

    @Value("${count.url}")
    private String countUrl;

    @Value("${title.url}")
    private String titleUrl;

    @Override
    public Doc getDoc(String id, CommonParam commonParam) {
        Doc doc = new Doc();
        doc.setId(id);

        int count = new CountCommand(id, countUrl, commonParam).execute();
        String title = new TitleCommand(id, titleUrl, commonParam).execute();
        doc.setCount(count).setTitle(title);
        return doc;
    }
}
