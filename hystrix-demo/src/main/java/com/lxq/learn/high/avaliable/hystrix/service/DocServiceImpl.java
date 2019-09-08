package com.lxq.learn.high.avaliable.hystrix.service;

import com.lxq.learn.high.avaliable.common.dto.CommonParam;
import com.lxq.learn.high.avaliable.common.dto.Doc;
import com.lxq.learn.high.avaliable.hystrix.demo.FallbackDemoCommand;
import com.lxq.learn.high.avaliable.hystrix.service.count.CountService;
import com.lxq.learn.high.avaliable.hystrix.service.title.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lxq
 * @create 2019/9/3 22:43
 */
@Service
public class DocServiceImpl implements DocService {
    @Autowired
    private CountService countService;
    @Autowired
    private TitleService titleService;

    @Value("${count.url}")
    private String countUrl;

    @Override
    public Doc getDoc(String id, CommonParam commonParam) {
        Doc doc = new Doc();
        doc.setId(id);

        int count = new FallbackDemoCommand("1", countUrl, commonParam).execute();
        doc.setCount(count);
        //doc.setTitle(titleService.getTitle(id, commonParam));
        return doc;
    }
}
