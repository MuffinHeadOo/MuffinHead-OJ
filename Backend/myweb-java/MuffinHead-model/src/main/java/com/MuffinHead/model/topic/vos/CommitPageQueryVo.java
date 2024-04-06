package com.MuffinHead.model.topic.vos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitPageQueryVo implements Serializable {
    //id
    private Integer id;
    //id
    private Integer topicId;
    //标题名称
    private String topic;
    //提交者名称
    private Integer userId;
    //提交者名称
    private String userName;
    //提交语言
    private String language;
    //结果
    private Integer result;
    //运行时间
    private Integer executionTime;
    //运行内存
    private Integer memory;
    //提交时间
    private Date submitTime;
}
