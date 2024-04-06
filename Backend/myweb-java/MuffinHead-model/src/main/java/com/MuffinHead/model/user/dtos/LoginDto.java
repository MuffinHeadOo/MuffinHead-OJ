package com.MuffinHead.model.user.dtos;

import lombok.Data;

@Data

public class LoginDto {
    //账户
    private String name;
    //密码
    private String password;
}
