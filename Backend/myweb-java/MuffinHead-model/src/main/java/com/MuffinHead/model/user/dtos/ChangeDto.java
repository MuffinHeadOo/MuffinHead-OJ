package com.MuffinHead.model.user.dtos;

import lombok.Data;



//修改个人信息
@Data
public class ChangeDto {
    //手机号
    private String phone;
    //手机号验证码
    private String phoneCode;
    //头像
    private String image;
    //个性签名
    private String signature;

}
