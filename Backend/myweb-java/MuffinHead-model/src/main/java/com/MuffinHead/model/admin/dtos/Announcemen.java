package com.MuffinHead.model.admin.dtos;

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
public class Announcemen{
    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String topic;

    /**
     * 出题人id
     */
    private Integer userId;
    /**
     * 内容
     */
    private String content;
    /**
     * 出题人名称
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date createdTime;
}