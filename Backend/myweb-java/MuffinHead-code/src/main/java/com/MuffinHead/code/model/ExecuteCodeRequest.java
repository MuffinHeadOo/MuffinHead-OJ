package com.MuffinHead.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 执行代码请求
 *
 * @author zzx
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest
{

//    private List<String> inputList;

    private String code;

    private String language;



    private Integer topicId;

    private Integer userId;

    private Integer result;

    private Integer executionTime;

    private Integer memory;

    private Date submitTime;

    private String output;

    private String input;

    private Integer id;
}
