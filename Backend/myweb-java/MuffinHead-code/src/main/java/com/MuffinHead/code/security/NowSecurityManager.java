package com.MuffinHead.code.security;

/**
 * 安全权限管理
 *
 * @author zzx
 */
public class NowSecurityManager extends SecurityManager
{

    /**
     * 检测程序是否可执行文件
     *
     * @param cmd
     */
    @Override
    public void checkExec(String cmd)
    {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    /**
     * 检测程序是否允许读文件
     *
     * @param file
     */
    @Override
    public void checkRead(String file)
    {
        // 允许读
        // throw new SecurityException("checkRead 权限异常：" + file);
    }


    /**
     * 检测程序是否允许写文件
     *
     * @param file
     */
    @Override
    public void checkWrite(String file)
    {
        throw new SecurityException("checkWrite 权限异常：" + file);
    }


    /**
     * 检测程序是否允许删除文件
     *
     * @param file
     */
    @Override
    public void checkDelete(String file)
    {
        throw new SecurityException("checkDelete 权限异常：" + file);
    }


    /**
     * 检测程序是否允许连接网络
     *
     * @param host
     * @param port
     */
    @Override
    public void checkConnect(String host, int port)
    {
        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }
}
