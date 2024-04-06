package com.MuffinHead.service.controller;


import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.topic.dtos.TopicPageQueryDTO;
import com.MuffinHead.model.user.dtos.ChangeDto;
import com.MuffinHead.model.user.dtos.ForgetPasswordDto;
import com.MuffinHead.model.user.dtos.LoginDto;
import com.MuffinHead.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserLoginController {

    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/loginAdmin")
    public ResponseResult loginAdmin(@RequestBody LoginDto loginDto) {
        return userService.loginAdmin(loginDto);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody LoginDto loginDto){
        return userService.register(loginDto);
    }

    @PostMapping("/forget")
    public ResponseResult forget(@RequestBody ForgetPasswordDto forgetPasswordDto){
        return userService.forget(forgetPasswordDto);
    }

    @PostMapping("/change")
    public ResponseResult change(@RequestBody ChangeDto changeDto){
        return userService.change(changeDto);
    }

    @GetMapping("/changeShowBack")
    public ResponseResult changeShowBack(){
        return userService.changeShowBack();
    }

}
