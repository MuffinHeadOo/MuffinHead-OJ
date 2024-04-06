package com.MuffinHead.model.admin.dtos;

import lombok.Data;

import java.util.List;


@Data
public class ChangeProblemDto {
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
    private List<String>  label;
}
