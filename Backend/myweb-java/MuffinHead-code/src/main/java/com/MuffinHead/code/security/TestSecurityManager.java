package com.MuffinHead.code.security;

import java.io.IOException;
import java.net.Socket;

/**
 * 测试安全管理器
 */
public class TestSecurityManager {

    public static void main(String[] args) {
        System.setSecurityManager(new NowSecurityManager());
        // 写文件
        // FileUtil.writeString("aa", "aaa", Charset.defaultCharset());
        // 删除文件
        // FileUtil.del("E:\\project\\sspu_oj\\new\\sspuoj_code_sandbox\\src\\main\\resources\\木马程序.bat");
        // 连接网络
        String host = "example.com"; // 替换为你想连接的主机
        int port = 80; // 替换为你想连接的端口

        try {
            // 尝试连接到指定主机和端口
            Socket socket = new Socket(host, port);

            // 如果连接成功，打印连接成功的消息
            System.out.println("Connection to " + host + ":" + port + " successful.");

            // 关闭连接
            socket.close();
        } catch (SecurityException e) {
            // 捕获SecurityException，即checkConnect权限异常
            System.err.println("SecurityException: " + e.getMessage());
        } catch (IOException e) {
            // 捕获IOException，即连接失败异常
            System.err.println("IOException: " + e.getMessage());
        }
    }
}
