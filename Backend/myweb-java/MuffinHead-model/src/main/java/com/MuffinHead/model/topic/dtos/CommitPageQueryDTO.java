package com.MuffinHead.model.topic.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CommitPageQueryDTO {

    //页码
    private int page;
    //每页记录数
    private int pageSize;


    //题目名称
    private String topic;
    //提交者
    private String userName;
    //结果
    private Integer result;
    //结果（失败的情况-3）
    private List<Integer> resultList;

}
