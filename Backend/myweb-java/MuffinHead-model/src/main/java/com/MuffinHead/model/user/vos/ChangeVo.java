package com.MuffinHead.model.user.vos;

import lombok.Data;


//修改个人信息
@Data
public class ChangeVo {
    //手机号
    private String phone;
    //头像
    private String image;
    //个性签名
    private String signature;

}
