package com.MuffinHead.service.service;

import com.MuffinHead.model.admin.dtos.SaveAdminChangeVo;
import com.MuffinHead.model.user.dtos.*;
import com.MuffinHead.model.user.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.MuffinHead.model.common.dtos.ResponseResult;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param dto
     * @return
     */
    ResponseResult login(LoginDto dto);
    /**
     * 用户注册
     * @param dto
     * @return
     */
    ResponseResult register(LoginDto dto);

    /**
     * 忘记密码
     * @param forgetPasswordDto
     * @return
     */
    ResponseResult forget(ForgetPasswordDto forgetPasswordDto);

    /**
     * 修改个人信息
     * @param changeDto
     * @return
     */
    ResponseResult change(ChangeDto changeDto);

    /**
     * 修改个人信息的数据回显
     * @return
     */
    ResponseResult changeShowBack();
    /**
     * 管理端修改个人信息的数据回显
     * @return
     */
    ResponseResult adminChangeShowBack(Integer id);

    /**
     * 保存管理端修改信息
     * @param saveAdminChangeVo
     * @return
     */
    ResponseResult saveAdminChangeShowBack(SaveAdminChangeVo saveAdminChangeVo);

    /**
     * 新，修改个人信息
     * @param userChangeDto
     * @return
     */
    ResponseResult changeMessage(UserChangeDto userChangeDto);

    /**
     * 用户修改密码
     * @param userChangePwdDto
     * @return
     */
    ResponseResult changePwd(UserChangePwdDto userChangePwdDto);

    /**
     * 主页AC列表查询
     * @return
     */
    ResponseResult accepted();

    /**
     * 管理员登录
     * @param loginDto
     * @return
     */
    ResponseResult loginAdmin(LoginDto loginDto);


}
