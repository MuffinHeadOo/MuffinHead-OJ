package com.MuffinHead.service.mapper;

import com.MuffinHead.model.admin.dtos.Announcemen;
import com.MuffinHead.model.topic.dtos.CommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.MyCommitPageQueryDTO;
import com.MuffinHead.model.topic.dtos.TopicPageQueryDTO;
import com.MuffinHead.model.topic.vos.CommitDetailVo;
import com.MuffinHead.model.topic.vos.CommitPageQueryVo;
import com.MuffinHead.model.topic.vos.TopicContentVo;
import com.MuffinHead.model.topic.vos.TopicPageQueryVo;
import com.github.pagehelper.Page;
import net.sf.jsqlparser.statement.Commit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomeMapper {
    /**
     * 分页查询题目
     * @param topicPageQueryDTO
     * @return
     */
    Page<TopicPageQueryVo> list(TopicPageQueryDTO topicPageQueryDTO);

    /**
     * 查询题目信息
     * @param id
     * @return
     */
    TopicContentVo getTopic(Integer id);

    /**
     * 保存判决前的信息到数据库
     * @param commit
     */
    void commit(Commit commit);

    /**
     * 提交+1
     * @param topicId
     */
    void updateCommit(Integer topicId);

    /**
     * 通过+1
     * @param topicId
     */

    void updateCommitAndAccepted(Integer topicId);

    /**
     * 分页条件查询提交信息
     * @param commitPageQueryDTO
     * @return
     */
    Page<CommitPageQueryVo> submissionsList(CommitPageQueryDTO commitPageQueryDTO);

    /**
     * 我的提交信息
     * @param myCommitPageQueryDTO
     * @return
     */
    Page<CommitPageQueryVo> mySubmissionsList(MyCommitPageQueryDTO myCommitPageQueryDTO);

    /**
     * 查询某次提交具体细节
     * @param id
     * @return
     */
    CommitDetailVo commitDetail(Integer id);

    List<Announcemen> announcement();

    /**
     * 展示公告具体内容
     * @param id
     * @return
     */
    List<Announcemen> announcementDetail(Integer id);
}
