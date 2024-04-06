package com.MuffinHead.model.admin.pojos;

import lombok.Data;

import java.util.List;


@Data
public class SaveChangeProblem {
    //id
    private Integer id;
    //topic标题
    private String topic;
    //content
    private String content;
    //input
    private String input;
    //output
    private String output;
    //label标签
    private String label;
}
