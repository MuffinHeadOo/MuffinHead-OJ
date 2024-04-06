package com.MuffinHead.service.mapper;

import com.MuffinHead.model.admin.dtos.Announcemen;
import com.MuffinHead.model.admin.dtos.AnnouncementPageRequestDto;
import com.MuffinHead.model.admin.dtos.ChangeAnnouncementDto;
import com.MuffinHead.model.admin.dtos.ChangeProblemMapper;
import com.MuffinHead.model.admin.pojos.AddProblem;
import com.MuffinHead.model.admin.pojos.SaveChangeProblem;
import com.MuffinHead.model.admin.vos.AnnouncementPageQueryVo;
import com.MuffinHead.model.topic.pojos.Topic;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    void delProblem(Integer id);

    void delContent(Integer id);

    void addProblem(Topic topic);


    void addProblemContent(AddProblem addProblem);

    ChangeProblemMapper problemChangeShowBack(Integer id);

    void saveProblem(SaveChangeProblem saveChangeProblem);

    Page<AnnouncementPageQueryVo> announcement(AnnouncementPageRequestDto announcementPageRequestDto);

    void delAnnouncement(Integer id);

    void addAnnouncemen(Announcemen announcemen);

    Announcemen announcemenChangeShowBack(Integer id);

    void saveAnnouncement(ChangeAnnouncementDto changeAnnouncementDto);

}
