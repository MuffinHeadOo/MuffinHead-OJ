package com.MuffinHead.model.admin.dtos;

import lombok.Data;

import java.util.List;


@Data
public class AddAnnouncementDto {
    //topic标题
    private String topic;
    //content
    private String content;
}
