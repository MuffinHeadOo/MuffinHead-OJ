package com.MuffinHead.model.topic.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 题目信息表
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("topic")
public class Topic implements Serializable {



    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题目名称
     */
    @TableField("topic")
    private String topic;

    /**
     * ac的数量
     */
    @TableField("accepted")
    private Integer accepted;

    /**
     * 提交的数量
     */
    @TableField("submit")
    private Integer submit;

    /**
     * 出题人id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 题目标签
     */
    @TableField("label")
    private String label;
}