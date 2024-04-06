package com.MuffinHead.model.topic.dtos;

import lombok.Data;

import java.util.List;

@Data
public class MyCommitPageQueryDTO {

    //页码
    private int page;
    //每页记录数
    private int pageSize;

    private Integer id;

    private Integer topicId;
}
