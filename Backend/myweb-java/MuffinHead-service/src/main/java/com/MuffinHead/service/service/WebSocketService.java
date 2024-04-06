package com.MuffinHead.service.service;


import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.service.websocket.pojo.Message;

public interface WebSocketService {

    Integer getUserId(String from);

    void save(Message message);

    ResponseResult getAll();
}
