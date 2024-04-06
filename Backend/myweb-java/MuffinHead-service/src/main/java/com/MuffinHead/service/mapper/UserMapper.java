package com.MuffinHead.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.MuffinHead.model.user.pojos.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
