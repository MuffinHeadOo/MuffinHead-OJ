package com.MuffinHead.service.controller;

import com.MuffinHead.code.model.ExecuteCodeRequest;
import com.MuffinHead.code.model.ExecuteCodeResponse;
import com.MuffinHead.code.model.enums.QuestionSubmitStatusEnum;
import com.MuffinHead.code.model.enums.SupportLanguageEnum;
import com.MuffinHead.service.service.java.JavaNativeCodeSandbox;
import com.MuffinHead.service.service.python3.Python3Native3CodeSandbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author ZZX
 * @Date 2024/1/19 8:17
 */
@RestController
@RequestMapping("/codesandbox")
public class CodeController
{

    @Autowired
    private JavaNativeCodeSandbox javaNativeCodeSandbox;
    @Autowired
    private Python3Native3CodeSandbox python3NativeCodeSandbox;


    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/run")
    public ExecuteCodeResponse runCode(@RequestBody ExecuteCodeRequest executeCodeRequest)
    {
        if (executeCodeRequest == null)
        {
            throw new RuntimeException("请求参数为空");
        }
        String language = executeCodeRequest.getLanguage();
        if (SupportLanguageEnum.JAVA.getValue().equals(language))
        {
            return javaNativeCodeSandbox.executeCode(executeCodeRequest);
        }
        else if (SupportLanguageEnum.PYTHON3.getValue().equals(language))
        {
            return python3NativeCodeSandbox.executeCode(executeCodeRequest);
        }
        else
        {
            return new ExecuteCodeResponse(null, "不支持的编程语言：" + language + "；当前仅支持：" + SupportLanguageEnum.getValues(), QuestionSubmitStatusEnum.FAILED.getValue(), null,null);
        }
    }



}