package com.MuffinHead.service.controller;

import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.model.admin.dtos.AdminUseDto;
import com.MuffinHead.model.admin.dtos.SaveAdminChangeVo;
import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.topic.dtos.CommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.MyCommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.TopicPageQueryDTO;
import com.MuffinHead.model.topic.vos.CommitDetailVo;
import com.MuffinHead.model.topic.vos.TopicContentVo;
import com.MuffinHead.model.user.dtos.UserChangeDto;
import com.MuffinHead.model.user.dtos.UserChangePwdDto;
import com.MuffinHead.model.user.pojos.User;
import com.MuffinHead.service.service.HomeService;
import com.MuffinHead.service.service.UserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody TopicPageQueryDTO topicPageQueryDTO){
        PageResult pageResult = homeService.list(topicPageQueryDTO);
        return ResponseResult.okResult(pageResult);
    }
    @GetMapping("/list/{id}")
    public ResponseResult getTopic(@PathVariable Integer id){
        TopicContentVo topicContentVo = homeService.getTopic(id);
        topicContentVo.setId(id);
        return ResponseResult.okResult(topicContentVo);
    }

    @PostMapping("/list/submission")
    public ResponseResult commit(@RequestBody CommitPageQueryDTO commitPageQueryDTO){
        PageResult pageResult = homeService.submissionsList(commitPageQueryDTO);
        return ResponseResult.okResult(pageResult);
    }

    @PostMapping("/list/mySubmission")
    public ResponseResult myCommit(@RequestBody MyCommitPageQueryDTO myCommitPageQueryDTO){
        PageResult pageResult = homeService.mySubmissionsList(myCommitPageQueryDTO);
        return ResponseResult.okResult(pageResult);
    }

    @GetMapping("/list/submission/{id}")
    public ResponseResult CommitDetail(@PathVariable Integer id){
        CommitDetailVo commitDetailVo = homeService.commitDetail(id);
        List<CommitDetailVo> commitDetailVoList = Arrays.asList(commitDetailVo);
        return ResponseResult.okResult(commitDetailVoList);
    }

    @PostMapping("/user")
    public ResponseResult getUser(){
        User user = userService.getById(BaseContext.getCurrentId());
        user.setSalt(null);
        user.setPassword(null);
        return ResponseResult.okResult(user);
    }

    @GetMapping("/user/{id}")
    public ResponseResult getUser(@PathVariable Integer id){
        String key = "home:user:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(org.apache.commons.lang3.StringUtils.isNotBlank(json)){
            User user = JSON.parseObject(json, User.class);
            //3.存在，直接返回
            return ResponseResult.okResult(user);
        }

        //4.不存在，查询数据库
        User user = userService.getById(id);
        user.setSalt(null);
        user.setPassword(null);
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(user),6, TimeUnit.HOURS);
        //6.返回
        return ResponseResult.okResult(user);
    }

    @PostMapping("/user/changeMessage")
    public ResponseResult changeMessage(@RequestBody UserChangeDto userChangeDto){
        return userService.changeMessage(userChangeDto);
    }

    @PostMapping("/user/changePwd")
    public ResponseResult changePwd(@RequestBody UserChangePwdDto userChangePwdDto){
        return userService.changePwd(userChangePwdDto);
    }

    @PostMapping("/accepted")
    public ResponseResult accepted(){
        return userService.accepted();
    }

    @PostMapping("/announcement")
    public ResponseResult announcement(){
        return homeService.announcement();
    }

    @GetMapping("/announcement/{id}")
    public ResponseResult announcementDetail(@PathVariable Integer id){
        return homeService.announcementDetail(id);
    }

}
