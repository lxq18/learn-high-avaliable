package com.lxq.learn.high.avaliable.common.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lxq
 * @create 2019/9/3 22:50
 */
@Getter
@Setter
@Accessors(chain = true)
public class CommonParam {
    /**
     * 延迟毫秒数
     */
    private Integer delayMilli;
    /**
     * 出现异常
     */
    private boolean occurException;

}
