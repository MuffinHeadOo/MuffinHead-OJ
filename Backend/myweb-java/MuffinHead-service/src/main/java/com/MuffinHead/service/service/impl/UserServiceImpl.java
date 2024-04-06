package com.MuffinHead.service.service.impl;

import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.model.admin.dtos.Announcemen;
import com.MuffinHead.model.admin.dtos.SaveAdminChangeVo;
import com.MuffinHead.model.admin.vos.AdminChangeVo;
import com.MuffinHead.model.admin.vos.ChangeAnnouncementVo;
import com.MuffinHead.model.user.dtos.*;
import com.MuffinHead.model.user.vos.ChangeVo;
import com.MuffinHead.service.mapper.UserMapper;
import com.MuffinHead.service.service.HomeService;
import com.MuffinHead.service.service.UserService;
import com.MuffinHead.service.utils.AppJwtUtil;
import com.MuffinHead.service.utils.PasswordUtils;
import com.MuffinHead.service.utils.RegexUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.common.enums.AppHttpCodeEnum;
import com.MuffinHead.model.user.pojos.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * app端用户登录
     *
     * @param dto
     * @return
     */
    @Override 
    public ResponseResult login(LoginDto dto) {
        //1.用户名或密码为空
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码为空");
        }
        //1.1根据账号查询用户信息
        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getName, dto.getName()));
        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该用户不存在->请先注册");
        }
        //1.2比对密码
           String salt = dbUser.getSalt();
           String password = dto.getPassword();
           String pswd = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        if (!pswd.equals(dbUser.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "密码错误");
        }
        //1.3返回jwt user
           String token = AppJwtUtil.getToken(dbUser.getId().longValue());
           Map<String,Object> map = new HashMap<>();
           map.put("token",token);
           dbUser.setSalt("");
           dbUser.setPassword("");
           map.put("user",dbUser);
        //感觉可以搞个vo的


        return ResponseResult.okResult(map);
//        return ResponseResult.okResult(map);
//        else {
//            //2.游客登录
//            Map<String,Object> map = new HashMap<>();
//            map.put("token",AppJwtUtil.getToken(0L));
//            return ResponseResult.okResult(map);
//        }

    }

    /**
     * 用户注册
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult register(LoginDto dto) {
        //1.用户名或密码为空
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码不能为空");
        }
        //2.判断用户是否存在
        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getName, dto.getName()));
        if (dbUser != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该用户已存在");
        }
        //账号密码长度限制（可以在前端）
        //注册
        User user = new User();
        BeanUtils.copyProperties(dto,user);
        user.setCreatedTime(new Date());
        //生产盐
        String salt = UUID.randomUUID().toString().replace("-","");
        user.setSalt(salt);
        //生成加盐后的密码(需要使用MD5)
        String saltPassword = DigestUtils.md5DigestAsHex((salt + dto.getPassword()).getBytes());
        user.setPassword(saltPassword);
        //默认权限0
        user.setAuthority(0);
        //默认状态0
        user.setStatus(0);
        //AC0
        user.setAccepted(0);
        save(user);
        return ResponseResult.okResult(user);
    }

    /**
     * 忘记密码
     *
     * @param forgetPasswordDto
     * @return
     */
    @Override
    public ResponseResult forget(ForgetPasswordDto forgetPasswordDto) {
        //1.检验用户名合法性
        //1.2判断是否为空
        if (StringUtils.isBlank(forgetPasswordDto.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名不能为空");
        }
        //1.3 判断是否存在
        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getName, forgetPasswordDto.getName()));
        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该用户不存在");
        }

        //2.检验手机号合法性
        //2.1判断是否为空  前端做格式？
        if (StringUtils.isBlank(forgetPasswordDto.getPhone())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "手机号不能为空");
        }
        //2.2 检验用户名与手机号是否匹配
        if(!forgetPasswordDto.getPhone().equals(dbUser.getPhone())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "手机号输入有误或与注册时使用的不同");
        }

        //3.匹配两个密码是否一致
        //3.1判断输入密码为空
        if (StringUtils.isBlank(forgetPasswordDto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "密码不能为空");
        }
        //3.2判断两个密码是否一致
        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getPassword2())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "两个密码不相同");
        }
        //4.更新新的密码，盐，到数据库
        //生产盐
        String salt = UUID.randomUUID().toString().replace("-","");
        dbUser.setSalt(salt);
        //生成加盐后的密码(需要使用MD5)
        String saltPassword = DigestUtils.md5DigestAsHex((salt + forgetPasswordDto.getPassword()).getBytes());
        dbUser.setPassword(saltPassword);

        updateById(dbUser);

        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 修改个人信息
     *
     * @param changeDto
     * @return
     */
    @Override
    public ResponseResult change(ChangeDto changeDto) {
        //1.检验手机号
        if(!RegexUtils.isMobileExact(changeDto.getPhone())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "手机号格式不正确");
        }

        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, changeDto.getPhone()));

        if(dbUser != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该手机号已存在");
        }
        //2.检验验证码

        //3.设置默认头像(注册时候设置没想好)
        //3.保存信息
        dbUser = new User();
        dbUser.setId(Math.toIntExact(BaseContext.getCurrentId()));
        dbUser.setPhone(changeDto.getPhone());
        dbUser.setImage(changeDto.getImage());
        dbUser.setSignature(changeDto.getSignature());

        updateById(dbUser);
        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 修改个人信息的数据回显
     *
     * @return
     */
    @Override
    public ResponseResult changeShowBack() {
        User user = userMapper.selectById(BaseContext.getCurrentId());
        ChangeVo changeVo = new ChangeVo();
        BeanUtils.copyProperties(user, changeVo);
        return ResponseResult.okResult(changeVo);
    }

    /**
     * 管理端修改个人信息的数据回显
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult adminChangeShowBack(Integer id) {
        String key = "admin:user:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(org.apache.commons.lang3.StringUtils.isNotBlank(json)){
            AdminChangeVo adminChangeVo = JSON.parseObject(json, AdminChangeVo.class);
            //3.存在，直接返回
            return ResponseResult.okResult(adminChangeVo);
        }

        //4.不存在，查询数据库
        User user = userMapper.selectById(id);
        AdminChangeVo adminChangeVo = new AdminChangeVo();
        BeanUtils.copyProperties(user, adminChangeVo);
        adminChangeVo.setPassword("********************");
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(adminChangeVo),6, TimeUnit.HOURS);
        //6.返回
        return ResponseResult.okResult(adminChangeVo);
    }

    /**
     * 保存管理端修改信息
     *
     * @param saveAdminChangeVo
     * @return
     */
    @Override
    public ResponseResult saveAdminChangeShowBack(SaveAdminChangeVo saveAdminChangeVo) {
        //1.用户名或密码为空
        if (StringUtils.isBlank(saveAdminChangeVo.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "密码不能为空");
        }
        User user = new User();
        BeanUtils.copyProperties(saveAdminChangeVo,user);
        user.setCreatedTime(saveAdminChangeVo.getCreatedTime());
        if(saveAdminChangeVo.getP() == 1){
            //生产盐
            String salt = UUID.randomUUID().toString().replace("-","");
            user.setSalt(salt);
            //生成加盐后的密码(需要使用MD5)
            String saltPassword = DigestUtils.md5DigestAsHex((salt + saveAdminChangeVo.getPassword()).getBytes());
            user.setPassword(saltPassword);
        }else {
            user.setPassword(null);
        }
        updateById(user);
        //2.删除缓存
        String key = "admin:user:" + user.getId();
        String key2 = "home:user:" + user.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);

        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 新，修改个人信息
     *
     * @param userChangeDto
     * @return
     */
    @Override
    public ResponseResult changeMessage(UserChangeDto userChangeDto) {
        //1.检验手机号
        if(!RegexUtils.isMobileExact(userChangeDto.getPhone())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "手机号格式不正确");
        }

        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, userChangeDto.getPhone()));

        if(dbUser != null && dbUser.getId() != Math.toIntExact(BaseContext.getCurrentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该手机号已存在");
        }

        //2.检验验证码

        //3.设置默认头像(注册时候设置没想好)
        //3.保存信息
        dbUser = new User();
        dbUser.setId(Math.toIntExact(BaseContext.getCurrentId()));
        dbUser.setPhone(userChangeDto.getPhone());
        dbUser.setImage(userChangeDto.getImage());
        dbUser.setSignature(userChangeDto.getSignature());
        dbUser.setEmail(userChangeDto.getEmail());

        updateById(dbUser);
        String key = "admin:user:" + dbUser.getId();
        String key2 = "home:user:" + dbUser.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 用户修改密码
     *
     * @param userChangePwdDto
     * @return
     */
    @Override
    public ResponseResult changePwd(UserChangePwdDto userChangePwdDto) {

        User user = getById(BaseContext.getCurrentId());


        //3.1判断输入密码为空
        if (StringUtils.isBlank(userChangePwdDto.getOldPwd()) || StringUtils.isBlank(userChangePwdDto.getNewPwd()) || StringUtils.isBlank(userChangePwdDto.getRepPwd())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "密码不能为空");
        }

        //1.2比对密码
        String salt = user.getSalt();
        String password = userChangePwdDto.getOldPwd();
        String pswd = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        if (!pswd.equals(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "旧密码错误");
        }

        //3.2判断两个密码是否一致
        if(!userChangePwdDto.getNewPwd().equals(userChangePwdDto.getRepPwd())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "两个密码不相同");
        }
        //4.更新新的密码，盐，到数据库
        //生产盐
        String newSalt = UUID.randomUUID().toString().replace("-","");
        user.setSalt(newSalt);
        //生成加盐后的密码(需要使用MD5)
        String saltPassword = DigestUtils.md5DigestAsHex((newSalt + userChangePwdDto.getNewPwd()).getBytes());
        user.setPassword(saltPassword);

        updateById(user);

        return ResponseResult.okResult(200,"修改成功");

    }

    /**
     * 主页AC列表查询
     *
     * @return
     */
    @Override
    public ResponseResult accepted() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 设置select方法，指定需要查询的字段
        queryWrapper.select("id", "name" , "signature" , "accepted");

        // 按照accepted字段倒序排序
        queryWrapper.orderByDesc("accepted");

        // 执行查询并获取结果列表
        List<User> userList = userMapper.selectList(queryWrapper);

        return ResponseResult.okResult(userList);
    }

    /**
     * 管理员登录
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loginAdmin(LoginDto dto) {
        //1.用户名或密码为空
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名或密码为空");
        }
        //1.1根据账号查询用户信息
        User dbUser = getOne(Wrappers.<User>lambdaQuery().eq(User::getName, dto.getName()));

        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该用户不存在->请先注册");
        }
        //1.2比对密码
        String salt = dbUser.getSalt();
        String password = dto.getPassword();
        String pswd = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        if (!pswd.equals(dbUser.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "密码错误");
        }
        //1.3
        if(dbUser.getAuthority() != 1){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "该用户不是管理员");
        }
        //1.3返回jwt user
        String token = AppJwtUtil.getToken(dbUser.getId().longValue());
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        dbUser.setSalt("");
        dbUser.setPassword("");
        map.put("user",dbUser);
        //感觉可以搞个vo的


        return ResponseResult.okResult(map);
    }


}
