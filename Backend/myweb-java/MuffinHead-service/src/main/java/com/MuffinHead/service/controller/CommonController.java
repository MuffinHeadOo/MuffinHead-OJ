package com.MuffinHead.service.controller;

import com.MuffinHead.model.common.dtos.ResponseResult;
import com.MuffinHead.service.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("文件上传");

        try {
            //获得原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀 xxx.jpg
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构建新的文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return ResponseResult.okResult(filePath);
        } catch (IOException e) {
            log.error("文件上传失败{}",e);
        }
        return ResponseResult.errorResult(500,"文件上传失败");
    }



}
