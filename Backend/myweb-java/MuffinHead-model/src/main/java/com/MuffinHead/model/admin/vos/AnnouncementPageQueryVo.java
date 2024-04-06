package com.MuffinHead.model.admin.vos;

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
public class AnnouncementPageQueryVo implements Serializable {
    //id
    private Integer id;
    //公告名称
    private String topic;
    //出题人id
    private Integer userId;
    //出题人
    private String userName;
    //发布时间
    private Date createdTime;

}
