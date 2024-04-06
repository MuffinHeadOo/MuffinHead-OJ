package com.MuffinHead.service.websocket.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Author:gzh
 * @Date: 2022/4/20 20:43
 */
@Data
public class Message {
        //    时间
        private String time;
        //    接收方
        private String to;
        //    发送方
        private String from;
        //    发送方
        private Integer userId;
        //    消息
        private String msg;
        //    登录用户名
        private List<String> userNames;
        //    头像url
        private String image;
}