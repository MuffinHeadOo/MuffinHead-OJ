package com.MuffinHead.service.mapper;

import com.MuffinHead.code.model.ExecuteCodeRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMapper {
    void save(ExecuteCodeRequest executeCodeRequest);
}
