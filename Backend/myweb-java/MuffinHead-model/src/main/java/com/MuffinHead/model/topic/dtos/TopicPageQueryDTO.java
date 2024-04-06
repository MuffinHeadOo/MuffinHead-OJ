package com.MuffinHead.model.topic.dtos;

import lombok.Data;


@Data
public class TopicPageQueryDTO {
    //页码
    private int page;
    //每页记录数
    private int pageSize;


    //题目名称
    private String topic;
    //出题人
    private String userName;

}
