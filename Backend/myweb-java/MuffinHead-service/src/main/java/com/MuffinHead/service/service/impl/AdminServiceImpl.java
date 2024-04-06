package com.MuffinHead.service.service.impl;

import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.model.admin.dtos.*;
import com.MuffinHead.model.admin.pojos.AddProblem;
import com.MuffinHead.model.admin.pojos.SaveChangeProblem;
import com.MuffinHead.model.admin.vos.AdminChangeVo;
import com.MuffinHead.model.admin.vos.AnnouncementPageQueryVo;
import com.MuffinHead.model.admin.vos.ChangeAnnouncementVo;
import com.MuffinHead.model.admin.vos.ChangeProblemVo;
import com.MuffinHead.model.common.dtos.PageRequestDto;
import com.MuffinHead.model.common.dtos.PageResponseResult;
import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.topic.pojos.Topic;
import com.MuffinHead.model.topic.vos.TopicPageQueryVo;
import com.MuffinHead.model.user.pojos.User;
import com.MuffinHead.service.mapper.AdminMapper;
import com.MuffinHead.service.mapper.UserMapper;
import com.MuffinHead.service.service.AdminService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
public class AdminServiceImpl extends ServiceImpl<UserMapper, User> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseResult updateUser(AdminUseDto adminUseDto) {
        User user = new User();
        BeanUtils.copyProperties(adminUseDto,user);
        //生产盐
        String salt = UUID.randomUUID().toString().replace("-","");
        user.setSalt(salt);
        //生成加盐后的密码(需要使用MD5)
        String saltPassword = DigestUtils.md5DigestAsHex((salt + adminUseDto.getPassword()).getBytes());
        user.setPassword(saltPassword);
        updateById(user);

        String key = "admin:user:" + adminUseDto.getId();
        String key2 = "home:user:" + adminUseDto.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"保存成功");
    }

    /**
     * 分页查询用户列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult userList(PageRequestDto dto) {
        //1.分页条件查询
        IPage page = new Page(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        //用户名模糊查询
        if(StringUtils.isNoneBlank(dto.getName())){
            lambdaQueryWrapper.like(User::getName,dto.getName());
        }
        //状态精确查询
        if(dto.getStatus() != null){
            lambdaQueryWrapper.eq(User::getStatus,dto.getStatus());
        }
        //权限精确查询
        if(dto.getAuthority()!=null){
            lambdaQueryWrapper.eq(User::getAuthority,dto.getAuthority());
        }
        //时间范围精确查询
        if(dto.getBeginDate() != null && dto.getEndDate()!= null){
            lambdaQueryWrapper.between(User::getCreatedTime,dto.getBeginDate(),dto.getEndDate());
        }
        //按照发布时间倒叙查询
        lambdaQueryWrapper.orderByDesc(User::getCreatedTime);

        IPage lastpage = page(page, lambdaQueryWrapper);
        //2.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) lastpage.getTotal());
        responseResult.setData(lastpage.getRecords());
        return responseResult;
    }

    /**
     * 修改用户状态
     *
     * @param changeStatusDto
     * @return
     */
    @Override
    public ResponseResult changeStatus(ChangeStatusDto changeStatusDto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", changeStatusDto.getId());
        updateWrapper.set("status", changeStatusDto.getStatus());
        baseMapper.update(null, updateWrapper);
        String key = "admin:user:" + changeStatusDto.getId();
        String key2 = "home:user:" + changeStatusDto.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 修改用户权限
     *
     * @param changeStatusDto
     * @return
     */
    @Override
    public ResponseResult changeAuthority(ChangeAuthorityDto changeStatusDto) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", changeStatusDto.getId());
        updateWrapper.set("authority", changeStatusDto.getAuthority());
        baseMapper.update(null, updateWrapper);
        String key = "admin:user:" + changeStatusDto.getId();
        String key2 = "home:user:" + changeStatusDto.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 根据id删除问题
     *
     * @param delProblemDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult deleteProblem(DelProblemDto delProblemDto) {
        adminMapper.delContent(delProblemDto.getId());
        adminMapper.delProblem(delProblemDto.getId());
        String key = "admin:topic:" + delProblemDto.getId();
        String key2 = "home:topic:" + delProblemDto.getId();
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"删除成功");
    }

    /**
     * 添加题目
     *
     * @param addProblemDto
     * @return
     */
    @Transactional
    @Override
    public ResponseResult addProblem(AddProblemDto addProblemDto) {
        Long id = BaseContext.getCurrentId();
        Topic topic = new Topic();
        topic.setTopic(addProblemDto.getTopic());
        topic.setAccepted(0);
        topic.setSubmit(0);
        topic.setUserId(Math.toIntExact(id));
        //获取当前系统date
        topic.setCreatedTime(new Date());
        topic.setLabel(String.join(",", addProblemDto.getLabel()));

        adminMapper.addProblem(topic);

        //
        AddProblem addProblem = new AddProblem();
        addProblem.setContent(addProblemDto.getContent());
        addProblem.setInput(addProblemDto.getInput());
        addProblem.setOutput(addProblemDto.getOutput());
        addProblem.setTopicId(topic.getId());
        System.out.println(topic.getId());

        adminMapper.addProblemContent(addProblem);

        return ResponseResult.okResult(200,"添加成功");
    }

    /**
     * 编辑题目信息数据回显
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult problemChangeShowBack(Integer id) {
        String key = "admin:topic:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(org.apache.commons.lang3.StringUtils.isNotBlank(json)){
            ChangeProblemVo changeProblemVo = JSON.parseObject(json, ChangeProblemVo.class);
            //3.存在，直接返回
            return ResponseResult.okResult(changeProblemVo);
        }

        //4.不存在，查询数据库
        ChangeProblemVo changeProblemVo = new ChangeProblemVo();
        ChangeProblemMapper changeProblemMapper = adminMapper.problemChangeShowBack(id);
        BeanUtils.copyProperties(changeProblemMapper,changeProblemVo);
        List<String> list = Arrays.asList(changeProblemMapper.getLabel().split(","));
        changeProblemVo.setLabel(list);
        changeProblemVo.setId(id);
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(changeProblemVo),6, TimeUnit.HOURS);
        //6.返回

        return ResponseResult.okResult(changeProblemVo);
    }

    /**
     * 保存编辑的题目信息
     *
     * @param changeProblemDto
     * @return
     */
    @Override
    public ResponseResult changeProblem(ChangeProblemDto changeProblemDto) {
        String key = "admin:topic:" + changeProblemDto.getId();
        String key2 = "home:topic:" + changeProblemDto.getId();
        SaveChangeProblem saveChangeProblem = new SaveChangeProblem();
        BeanUtils.copyProperties(changeProblemDto,saveChangeProblem);
        saveChangeProblem.setLabel(String.join(",", changeProblemDto.getLabel()));
        adminMapper.saveProblem(saveChangeProblem);
        //删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"修改成功");
    }

    /**
     * 主页分页查询公告
     *
     * @param announcementPageRequestDto
     * @return
     */
    @Override
    public PageResult announcement(AnnouncementPageRequestDto announcementPageRequestDto) {
        PageHelper.startPage(announcementPageRequestDto.getPage(),announcementPageRequestDto.getPageSize());
        com.github.pagehelper.Page<AnnouncementPageQueryVo> page = adminMapper.announcement(announcementPageRequestDto);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 管理端删除公告
     *
     * @param delAnnouncementDto
     * @return
     */
    @Override
    public ResponseResult announcementDel(DelAnnouncementDto delAnnouncementDto) {
        String key = "admin:announcement:" + delAnnouncementDto.getId();
        String key2 = "home:announcement:" + delAnnouncementDto.getId();
        adminMapper.delAnnouncement(delAnnouncementDto.getId());
        //2.删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"删除成功");
    }

    /**
     * 添加公告
     *
     * @param addAnnouncementDto
     * @return
     */
    @Override
    public ResponseResult addAnnouncemen(AddAnnouncementDto addAnnouncementDto) {
        Long id = BaseContext.getCurrentId();
        Announcemen announcemen = new Announcemen();
        announcemen.setTopic(addAnnouncementDto.getTopic());
        announcemen.setUserId(Math.toIntExact(id));
        //获取当前系统date
        announcemen.setCreatedTime(new Date());
        announcemen.setContent(addAnnouncementDto.getContent());

        adminMapper.addAnnouncemen(announcemen);

        return ResponseResult.okResult(200,"添加成功");
    }

    /**
     * 管理端编辑公告信息回显
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult announcementShowBack(Integer id) {
        String key = "admin:announcement:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StringUtils.isNotBlank(json)){
            ChangeAnnouncementVo changeAnnouncementVo = JSON.parseObject(json, ChangeAnnouncementVo.class);
            //3.存在，直接返回
            return ResponseResult.okResult(changeAnnouncementVo);
        }


        //4.不存在，查询数据库
        ChangeAnnouncementVo changeAnnouncementVo = new ChangeAnnouncementVo();
        Announcemen announcemen = adminMapper.announcemenChangeShowBack(id);
        BeanUtils.copyProperties(announcemen,changeAnnouncementVo);
        changeAnnouncementVo.setId(id);
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(changeAnnouncementVo),6, TimeUnit.HOURS);
        //6.返回

        return ResponseResult.okResult(changeAnnouncementVo);
    }

    /**
     * 管理端修改公告
     *
     * @param changeAnnouncementDto
     * @return
     */
    @Override
    public ResponseResult changeAnnouncement(ChangeAnnouncementDto changeAnnouncementDto) {
        String key = "admin:announcement:" + changeAnnouncementDto.getId();
        String key2 = "home:announcement:" + changeAnnouncementDto.getId();
        //1.更新数据库
        adminMapper.saveAnnouncement(changeAnnouncementDto);
        //2.删缓存
        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);
        return ResponseResult.okResult(200,"修改成功");

    }


}
