package com.MuffinHead.service.service;

import com.MuffinHead.model.common.dtos.PageResult;
import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.model.topic.dtos.CommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.MyCommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.TopicPageQueryDTO;
import com.MuffinHead.model.topic.vos.CommitDetailVo;
import com.MuffinHead.model.topic.vos.TopicContentVo;

public interface HomeService{

    /**
     * 条件查询题目
     * @param topicPageQueryDTO
     * @return
     */
    PageResult list(TopicPageQueryDTO topicPageQueryDTO);

    /**
     * 题目信息
     * @param id
     * @return
     */
    TopicContentVo getTopic(Integer id);

    /**
     * 分页查询提交信息
     * @param commitPageQueryDTO
     * @return
     */
    PageResult submissionsList(CommitPageQueryDTO commitPageQueryDTO);

    /**
     * 我的提交信息
     * @param myCommitPageQueryDTO
     * @return
     */
    PageResult mySubmissionsList(MyCommitPageQueryDTO myCommitPageQueryDTO);

    /**
     * 查询某次提交细节
     * @param id
     * @return
     */
    CommitDetailVo commitDetail(Integer id);

    /**
     * 主页展示公告
     * @return
     */
    ResponseResult announcement();

    /**
     * 展示公告具体内容
     * @param id
     * @return
     */
    ResponseResult announcementDetail(Integer id);
}
