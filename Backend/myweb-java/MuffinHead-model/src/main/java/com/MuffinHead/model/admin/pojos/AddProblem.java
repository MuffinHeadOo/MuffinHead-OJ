package com.MuffinHead.model.admin.pojos;

import lombok.Data;


@Data
public class AddProblem {
    //topicId
    private Integer topicId;
    //content
    private String content;
    //input
    private String input;
    //output
    private String output;
}
