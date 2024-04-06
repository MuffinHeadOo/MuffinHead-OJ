package com.MuffinHead.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

//mybatis plus分页
//用户分页查询
@Data
@Slf4j
public class PageRequestDto {

    private Integer size;
    private Integer page;



    /**
     * 用户名
     */
    private String name;
    /**
     * 权限
     */
    private Integer authority;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 开始时间
     */
    private Date beginDate;
    /**
     * 结束时间
     */
    private Date endDate;
}
