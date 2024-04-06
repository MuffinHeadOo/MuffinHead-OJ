package com.MuffinHead.model.user.dtos;

import lombok.Data;

@Data

public class ForgetPasswordDto {
    //账户
    private String name;
    //手机号
    private String phone;
    //新的密码
    private String password;
    //确认密码
    private String password2;
}
