package com.MuffinHead.model.topic.vos;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class TopicContentVo {
    private Integer id;
    //标题名称
    private String topic;
    //ac的数量
    private Integer accepted;
    //提交的数量
    private Integer submit;
    //标签
    private String label;
    //发布时间
    private Date createdTime;
    //出题人
    private String userName;
    //内容
    private String content;
    //input
    private String input;
    //output
    private String output;
}
