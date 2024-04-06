package com.MuffinHead.model.admin.dtos;

import lombok.Data;

import java.util.Date;

//修改个人信息
@Data
public class AdminUseDto {
    //id
    private Integer id;
    //头像
    private String image;
    //用户名
    private String name;
    //密码
    private String password;
    //注册时间
    private Date createdTime;
    //手机号
    private String phone;
    //个人主页
    private String signature;
}
