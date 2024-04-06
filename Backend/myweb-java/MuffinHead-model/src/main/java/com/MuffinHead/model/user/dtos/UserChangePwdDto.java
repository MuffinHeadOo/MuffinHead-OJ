package com.MuffinHead.model.user.dtos;
import lombok.Data;


//修改个人信息
@Data
public class UserChangePwdDto {
    //头像
    private String oldPwd;
    //手机号
    private String newPwd;
    //个性签名主页
    private String repPwd;

}
