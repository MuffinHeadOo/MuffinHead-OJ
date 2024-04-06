package com.MuffinHead.model.admin.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


//修改个人信息
@Data
public class SaveAdminChangeVo {
    //id
    private Integer id;
    //头像
    private String image;
    //用户名
    private String name;
    //密码
    private String password;
    //注册时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;
    //手机号
    private String phone;
    //个性签名主页
    private String signature;
    //手机号
    private String email;
    // 是否修改过密码 0未修改  1修改了
    private Integer p;
}
