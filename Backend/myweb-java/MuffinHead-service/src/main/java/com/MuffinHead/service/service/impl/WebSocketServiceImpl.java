package com.MuffinHead.service.service.impl;

import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.service.mapper.WebSocketMapper;
import com.MuffinHead.service.service.WebSocketService;
import com.MuffinHead.service.websocket.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private WebSocketMapper webSocketMapper;

    @Override
    public Integer getUserId(String from) {
        return webSocketMapper.getUserId(from);
    }

    @Override
    public void save(Message message) {
        webSocketMapper.save(message);
    }

    @Override
    public ResponseResult getAll() {
        List<Message> message = webSocketMapper.getAll();
        return ResponseResult.okResult(message);
    }
}
