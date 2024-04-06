package com.MuffinHead.model.user.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


//修改个人信息
@Data
public class UserChangeDto {
    //id
    private Integer id;
    //头像
    private String image;
    //手机号
    private String phone;
    //个性签名主页
    private String signature;
    //邮箱
    private String email;
}
