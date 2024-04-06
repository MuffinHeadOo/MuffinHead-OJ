package com.MuffinHead.code.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 判题信息
 * @author zzx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeInfo
{

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存(kb)
     */
    private Long memory;

    /**
     * 消耗时间(ms)
     */
    private Long time;
}
