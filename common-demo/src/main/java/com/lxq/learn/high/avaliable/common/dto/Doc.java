package com.lxq.learn.high.avaliable.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lxq
 * @create 2019/9/3 22:37
 */
@Getter
@Setter
@Accessors(chain = true)
public class Doc {
    private String id;
    private String title;
    private Integer count;
}
