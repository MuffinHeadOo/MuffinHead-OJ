package com.MuffinHead.service.controller;


import com.MuffinHead.model.admin.dtos.*;
import com.MuffinHead.model.common.dtos.PageRequestDto;
import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.service.service.AdminService;
import com.MuffinHead.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public ResponseResult userList(@RequestBody PageRequestDto pageRequestDto){
        return adminService.userList(pageRequestDto);
    }
    @PostMapping("/user/save")
    public ResponseResult updateUser(@RequestBody AdminUseDto adminUseDto){
        return adminService.updateUser(adminUseDto);
    }
    @PostMapping("/user/cs")
    public ResponseResult changeStatus(@RequestBody ChangeStatusDto changeStatusDto){
        return adminService.changeStatus(changeStatusDto);
    }
    @PostMapping("/user/ca")
    public ResponseResult changeAuthority(@RequestBody ChangeAuthorityDto changeStatusDto){
        return adminService.changeAuthority(changeStatusDto);
    }
    @GetMapping("/user/show/{id}")
    public ResponseResult show(@PathVariable Integer id){
        return userService.adminChangeShowBack(id);
    }
    @PostMapping("/user/show/save")
    public ResponseResult showSave(@RequestBody SaveAdminChangeVo saveAdminChangeVo){
        return userService.saveAdminChangeShowBack(saveAdminChangeVo);
    }
    @PostMapping("/topic/del")
    public ResponseResult DeleteProblem(@RequestBody DelProblemDto delProblemDto){
        return adminService.deleteProblem(delProblemDto);
    }
    @PostMapping("/topic/add")
    public ResponseResult AddProblem(@RequestBody AddProblemDto addProblemDto){
        return adminService.addProblem(addProblemDto);
    }
    @PostMapping("/topic/change")
    public ResponseResult ChangeProblem(@RequestBody ChangeProblemDto changeProblemDto){
        return adminService.changeProblem(changeProblemDto);
    }
    @GetMapping("/topic/show/{id}")
    public ResponseResult problemShowBack(@PathVariable Integer id){
        return adminService.problemChangeShowBack(id);
    }

    @PostMapping("/announcement")
    public ResponseResult announcement(@RequestBody AnnouncementPageRequestDto announcementPageRequestDto){
        PageResult pageResult = adminService.announcement(announcementPageRequestDto);
        return ResponseResult.okResult(pageResult);
    }

    @PostMapping("/announcement/del")
    public ResponseResult announcementDel(@RequestBody DelAnnouncementDto delAnnouncementDto){
        return adminService.announcementDel(delAnnouncementDto);
    }

    @PostMapping("/announcement/add")
    public ResponseResult AddAnnouncement(@RequestBody AddAnnouncementDto addAnnouncementDto){
        return adminService.addAnnouncemen(addAnnouncementDto);
    }

    @GetMapping("/announcement/show/{id}")
    public ResponseResult AnnouncementShowBack(@PathVariable Integer id){
        return adminService.announcementShowBack(id);
    }
    @PostMapping("/announcement/change")
    public ResponseResult ChangeAnnouncement(@RequestBody ChangeAnnouncementDto changeAnnouncementDto){
        return adminService.changeAnnouncement(changeAnnouncementDto);
    }



}
