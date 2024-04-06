package com.MuffinHead.model.admin.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

//mybatis plus分页
//用户分页查询
@Data
@Slf4j
public class AnnouncementPageRequestDto {

    //页码
    private int page;
    //每页记录数
    private int pageSize;



    /**
     * 公告名称
     */
    private String topic;
    /**
     * 发布者
     */

    private String userName;
}
