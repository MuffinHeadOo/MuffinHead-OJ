package com.MuffinHead.service.service;


import com.MuffinHead.code.model.ExecuteCodeRequest;
import com.MuffinHead.code.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author zzx
 */
public interface CodeSandbox
{

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
