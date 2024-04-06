package com.MuffinHead.model.admin.vos;

import lombok.Data;

import java.util.List;


@Data
public class ChangeAnnouncementVo {
    //id
    private Integer id;
    //topic标题
    private String topic;
    //content
    private String content;
    //id
    private Integer userId;
    //username
    private String userName;

}
