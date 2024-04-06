package com.MuffinHead.service.service;


import com.MuffinHead.model.admin.dtos.*;
import com.MuffinHead.model.common.dtos.PageRequestDto;
import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.user.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<User> {

    /**
     * 修改用户信息
     * @param adminUseDto
     * @return
     */
    ResponseResult updateUser(AdminUseDto adminUseDto);

    /**
     * 分页查询用户列表
     * @param pageRequestDto
     * @return
     */
    ResponseResult userList(PageRequestDto pageRequestDto);

    /**
     * 修改用户状态
     * @param changeStatusDto
     * @return
     */
    ResponseResult changeStatus(ChangeStatusDto changeStatusDto);
    /**
     * 修改用户权限
     * @param changeStatusDto
     * @return
     */
    ResponseResult changeAuthority(ChangeAuthorityDto changeStatusDto);



    /**
     * 根据id删除问题
     * @param delProblemDto
     * @return
     */
    ResponseResult deleteProblem(DelProblemDto delProblemDto);

    /**
     * 添加题目
     * @param addProblemDto
     * @return
     */
    ResponseResult addProblem(AddProblemDto addProblemDto);

    /**
     * 编辑题目信息数据回显
     * @param id
     * @return
     */
    ResponseResult problemChangeShowBack(Integer id);

    /**
     * 保存编辑的题目信息
     * @param changeProblemDto
     * @return
     */
    ResponseResult changeProblem(ChangeProblemDto changeProblemDto);

    /**
     * 主页分页查询公告
     * @param announcementPageRequestDto
     * @return
     */
    PageResult announcement(AnnouncementPageRequestDto announcementPageRequestDto);

    /**
     * 管理端删除公告
     * @param delAnnouncementDto
     * @return
     */
    ResponseResult announcementDel(DelAnnouncementDto delAnnouncementDto);

    /**
     * 添加公告
     * @param addAnnouncementDto
     * @return
     */
    ResponseResult addAnnouncemen(AddAnnouncementDto addAnnouncementDto);

    /**
     * 管理端编辑公告信息回显
     * @param id
     * @return
     */
    ResponseResult announcementShowBack(Integer id);

    /**
     * 管理端修改公告
     * @param changeAnnouncementDto
     * @return
     */
    ResponseResult changeAnnouncement(ChangeAnnouncementDto changeAnnouncementDto);
}
