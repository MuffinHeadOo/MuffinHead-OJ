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
public class TopicPageQueryVo implements Serializable {
    //id
    private Integer id;
    //标题名称
    private String topic;
    //AC数量
    private Integer accepted;
    //提交总数量
    private Integer submit;
    //发布时间
    private Date createdTime;
    //出题人
    private String userName;
    //出题人
    private Integer userId;
}
