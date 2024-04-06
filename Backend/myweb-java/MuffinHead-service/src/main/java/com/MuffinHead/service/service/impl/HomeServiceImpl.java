package com.MuffinHead.service.service.impl;
import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.model.admin.dtos.Announcemen;
import com.MuffinHead.model.admin.dtos.ChangeProblemMapper;
import com.MuffinHead.model.admin.vos.ChangeProblemVo;
import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.topic.dtos.CommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.MyCommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.TopicPageQueryDTO;
import com.MuffinHead.model.topic.vos.CommitDetailVo;
import com.MuffinHead.model.topic.vos.CommitPageQueryVo;
import com.MuffinHead.model.topic.vos.TopicContentVo;
import com.MuffinHead.model.topic.vos.TopicPageQueryVo;
import com.MuffinHead.model.user.pojos.User;
import com.MuffinHead.service.mapper.HomeMapper;
import com.MuffinHead.service.service.HomeService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@Transactional
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 条件查询题目
     *
     * @param topicPageQueryDTO
     * @return
     */
    @Override
    public PageResult list(TopicPageQueryDTO topicPageQueryDTO) {
        PageHelper.startPage(topicPageQueryDTO.getPage(),topicPageQueryDTO.getPageSize());
        Page<TopicPageQueryVo> page = homeMapper.list(topicPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 题目信息
     *
     * @param id
     * @return
     */
    @Override
    public TopicContentVo getTopic(Integer id) {
        String key = "home:topic:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(org.apache.commons.lang3.StringUtils.isNotBlank(json)){
            TopicContentVo topicContentVo = JSON.parseObject(json, TopicContentVo.class);
            //3.存在，直接返回
            return topicContentVo;
        }

        //4.不存在，查询数据库
        TopicContentVo topicContentVo = homeMapper.getTopic(id);
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(topicContentVo),6, TimeUnit.HOURS);
        //6.返回
        return topicContentVo;

    }

    /**
     * 分页查询提交信息
     *
     * @param commitPageQueryDTO
     * @return
     */
    @Override
    public PageResult submissionsList(CommitPageQueryDTO commitPageQueryDTO) {
        if(commitPageQueryDTO.getResult() != null && commitPageQueryDTO.getResult() == -3 ){
            List<Integer> resultList = Arrays.asList(-4,-2, -1, 100);
            commitPageQueryDTO.setResultList(resultList);
            commitPageQueryDTO.setResult(null);
        }
        PageHelper.startPage(commitPageQueryDTO.getPage(),commitPageQueryDTO.getPageSize());
        Page<CommitPageQueryVo> page = homeMapper.submissionsList(commitPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 我的提交信息
     *
     * @param myCommitPageQueryDTO
     * @return
     */
    @Override
    public PageResult mySubmissionsList(MyCommitPageQueryDTO myCommitPageQueryDTO) {
        myCommitPageQueryDTO.setId(Math.toIntExact(BaseContext.getCurrentId()));
        System.out.println(myCommitPageQueryDTO.getId());
        PageHelper.startPage(myCommitPageQueryDTO.getPage(),myCommitPageQueryDTO.getPageSize());
        Page<CommitPageQueryVo> page = homeMapper.mySubmissionsList(myCommitPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 查询某次提交细节
     *
     * @param id
     * @return
     */
    @Override
    public CommitDetailVo commitDetail(Integer id) {
        CommitDetailVo commitDetailVo = homeMapper.commitDetail(id);
        return commitDetailVo;
    }

    /**
     * 主页展示公告
     *
     * @return
     */
    @Override
    public ResponseResult announcement() {
        List<Announcemen> announcement = homeMapper.announcement();
        return ResponseResult.okResult(announcement);
    }

    /**
     * 展示公告具体内容
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult announcementDetail(Integer id) {
        String key = "home:announcement:" + id;

        //1.从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(org.apache.commons.lang3.StringUtils.isNotBlank(json)){
            List<Announcemen> announcement = JSON.parseObject(json, List.class);
            //3.存在，直接返回
            return ResponseResult.okResult(announcement);
        }

        //4.不存在，查询数据库
        List<Announcemen> announcement = homeMapper.announcementDetail(id);
        //5.将数据存入redis
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(announcement),6, TimeUnit.HOURS);
        //6.返回
        return ResponseResult.okResult(announcement);
    }


}
