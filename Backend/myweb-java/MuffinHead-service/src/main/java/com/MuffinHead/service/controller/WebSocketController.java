package com.MuffinHead.service.controller;

import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.service.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
@Slf4j
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;
    @PostMapping()
    public ResponseResult getMessage() {
        return webSocketService.getAll();
    }
}
