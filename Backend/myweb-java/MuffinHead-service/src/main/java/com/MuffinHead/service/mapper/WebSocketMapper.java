package com.MuffinHead.service.mapper;

import com.MuffinHead.model.admin.dtos.Announcemen;
import com.MuffinHead.model.admin.dtos.AnnouncementPageRequestDto;
import com.MuffinHead.model.admin.dtos.ChangeAnnouncementDto;
import com.MuffinHead.model.admin.dtos.ChangeProblemMapper;
import com.MuffinHead.model.admin.pojos.AddProblem;
import com.MuffinHead.model.admin.pojos.SaveChangeProblem;
import com.MuffinHead.model.admin.vos.AnnouncementPageQueryVo;
import com.MuffinHead.model.topic.pojos.Topic;
import com.MuffinHead.service.websocket.pojo.Message;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebSocketMapper {


    Integer getUserId(String from);

    void save(Message message);


    List<Message> getAll();
}
