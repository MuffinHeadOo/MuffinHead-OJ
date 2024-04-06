package com.MuffinHead.service.service.java;

import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.MuffinHead.code.model.ExecuteCodeRequest;
import com.MuffinHead.code.model.ExecuteCodeResponse;
import com.MuffinHead.code.model.ExecuteMessage;
import com.MuffinHead.code.model.JudgeInfo;
import com.MuffinHead.code.model.enums.JudgeInfoMessageEnum;
import com.MuffinHead.code.model.enums.QuestionSubmitStatusEnum;
import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.model.user.pojos.User;
import com.MuffinHead.service.mapper.CodeMapper;
import com.MuffinHead.service.mapper.HomeMapper;
import com.MuffinHead.service.mapper.UserMapper;
import com.MuffinHead.service.service.CodeSandbox;
import com.MuffinHead.service.service.CommonCodeSandboxTemplate;
import com.MuffinHead.service.utils.ProcessUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * Java 代码沙箱模板方法的实现
 *
 * @author zzx
 */
@Slf4j

public abstract class JavaCodeSandboxTemplate extends CommonCodeSandboxTemplate implements CodeSandbox
{

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private HomeMapper homeMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 待运行代码的文件夹路径名称
     */
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    /**
     * 待运行代码的存放文件名
     */
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    /**
     * 代码最大允许运行的时间
     */
    private static final long TIME_OUT = 10000L;

    /**
     * Java安全管理器类存放路径
     */
    private static final String SECURITY_MANAGER_PATH;

    /**
     * Java安全管理器类名
     */
    private static final String SECURITY_MANAGER_CLASS_NAME = "NowSecurityManager";

    /**
     * Java代码黑名单
     * 黑名单检测通常用于辅助安全策略，而不是作为唯一的安全手段
     */
    private static final List<String> blackList = Arrays.asList(
            // 文件操作相关
            "Files", "File", "FileInputStream", "FileOutputStream", "RandomAccessFile", "FileReader", "FileWriter", "FileChannel", "FileLock", "Path", "Paths", "File.createTempFile", "File.createTempDirectory", "ZipInputStream", "ZipOutputStream",

            // 网络相关
            "Socket", "ServerSocket", "DatagramSocket", "InetAddress", "URL", "URLConnection", "HttpURLConnection", "SocketChannel", "ServerSocketChannel", "DatagramChannel", "SocketPermission", "ServerSocketPermission",

            // 系统命令执行相关
            "exec", "Runtime.getRuntime().exec", "ProcessBuilder", "SecurityManager", "System.exit", "Runtime.getRuntime().halt", "SecurityManager.checkExec",

            // 反射相关
            "Class.forName", "Method.invoke", "sun.reflect.", "java.lang.reflect.", "Unsafe", "sun.misc.Unsafe", "sun.reflect.Unsafe", "Proxy",

            // 数据库相关
            "Statement", "PreparedStatement", "CallableStatement", "DataSource", "Connection", "ResultSet", "Hibernate", "JPA", // 防止使用 ORM 框架执行不安全的数据库操作
            "createStatement", "prepareStatement", "prepareCall",

            // 不安全的操作
            "Unsafe", "sun.misc.Unsafe", "sun.reflect.Unsafe",

            // 加密解密相关
            "Cipher", "MessageDigest", "KeyGenerator", "KeyPairGenerator", "SecretKeyFactory", "KeyStore", "SecureRandom", "java.security.",

            // 序列化相关
            "ObjectInputStream", "ObjectOutputStream", "Serializable", "Externalizable", "readObject", "writeObject",

            // 线程相关
            "Thread", "Runnable", "Executor", "ExecutorService", "ThreadPoolExecutor", "ThreadGroup", "ThreadLocal", "Thread.sleep", "Thread.yield", "Thread.stop", "Thread.suspend", "Thread.resume", "java.util.concurrent.",

            // 安全管理器相关
            "SecurityManager",

            // 其他可能导致安全问题的操作
            "System.load", "System.loadLibrary", // 防止加载本地库
            "JNI", "Java Native Interface", // 防止使用 JNI 调用本地代码
            "Unsafe.allocateMemory", "Unsafe.freeMemory", // 直接内存操作
            "System.getProperties", "System.setProperty", // 系统属性操作
            "System.getenv", // 获取环境变量
            "System.console", // 控制台访问
            "Runtime.addShutdownHook", // 添加关闭钩子
            "Runtime.load", "Runtime.loadLibrary" // 加载本地库
    );

    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;

    static
    {
        // 初始化黑名单字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blackList);
        // 初始安全配置文件路径
        SECURITY_MANAGER_PATH = System.getProperty("user.dir");
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        String[] inputList2 = executeCodeRequest.getInput().split("\\+");
        List<String> inputList = new ArrayList<>(Arrays.asList(inputList2));
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        System.out.println("当前操作系统：" + System.getProperty("os.name").toLowerCase());
        System.out.println("当前代码使用语言：" + language);

        // 0. 安全控制：限制敏感代码：黑名单检测
        FoundWord foundWord = WORD_TREE.matchWord(code);

        if (foundWord != null)
        {
            System.out.println("包含禁止词：" + foundWord.getFoundWord());
            // 返回错误信息
            return new ExecuteCodeResponse(null, "包含禁止词：" + foundWord.getFoundWord(), QuestionSubmitStatusEnum.FAILED.getValue(), new JudgeInfo(JudgeInfoMessageEnum.DANGEROUS_OPERATION.getValue(), null, null),null);
        }

        //设置提交者
        executeCodeRequest.setUserId(Math.toIntExact(BaseContext.getCurrentId()));


        // 1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code, GLOBAL_CODE_DIR_NAME, GLOBAL_JAVA_CLASS_NAME);

        // 2. 编译代码，得到 class 文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println("编译结果：" + compileFileExecuteMessage);
        if (compileFileExecuteMessage.getErrorMessage() != null)
        {
            executeCodeRequest.setResult(-2);
            executeCodeRequest.setExecutionTime(null);
            executeCodeRequest.setMemory(null);
            executeCodeRequest.setSubmitTime(new Date());
            codeMapper.save(executeCodeRequest);
            homeMapper.updateCommit(executeCodeRequest.getTopicId());
            //删除文件
            boolean b = deleteFile(userCodeFile);
            if (!b)
            {
                log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
            }
            // 返回编译错误信息
            return new ExecuteCodeResponse(null, compileFileExecuteMessage.getMessage(), QuestionSubmitStatusEnum.FAILED.getValue(), new JudgeInfo(compileFileExecuteMessage.getErrorMessage(), null, null),executeCodeRequest.getId());
        }

        // 3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);

        // 4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList);

        //运行超时
        if(outputResponse.getMessage().equals("Runtime Error")){
            boolean b = deleteFile(userCodeFile);
            if (!b)
            {
                log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
            }
            executeCodeRequest.setResult(-1);
            executeCodeRequest.setExecutionTime(Math.toIntExact(outputResponse.getJudgeInfo().getTime()));
            executeCodeRequest.setMemory(Math.toIntExact(outputResponse.getJudgeInfo().getMemory()));
            executeCodeRequest.setSubmitTime(new Date());
            codeMapper.save(executeCodeRequest);
            homeMapper.updateCommit(executeCodeRequest.getTopicId());
            return new ExecuteCodeResponse(null, outputResponse.getMessage(), QuestionSubmitStatusEnum.FAILED.getValue(), new JudgeInfo("运行超时", outputResponse.getJudgeInfo().getMemory(), outputResponse.getJudgeInfo().getTime()),executeCodeRequest.getId());

        }
        // 5. 文件清理
        boolean b = deleteFile(userCodeFile);
        if (!b)
        {
            log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        //计算得分
        // 使用加号作为分隔符来分割字符串
        String[] splitInput = executeCodeRequest.getOutput().split("\\+");
        // 将数组转换为List<String>
        List<String> outPutAnswer = Arrays.asList(splitInput);
        //判分
        List<String> outputList = outputResponse.getOutputList();
        int sum = 0;
        for(int i=0;i<outPutAnswer.size();i++){
            if(outputList.get(i).equals(outPutAnswer.get(i))){
                sum++;
            }
        }
        double result2 = (double) sum / outPutAnswer.size();
        int result = (int) (result2 * 100);
        executeCodeRequest.setResult(result);
        executeCodeRequest.setExecutionTime(Math.toIntExact(outputResponse.getJudgeInfo().getTime()));
        executeCodeRequest.setMemory(Math.toIntExact(outputResponse.getJudgeInfo().getMemory()));
        executeCodeRequest.setSubmitTime(new Date());
        codeMapper.save(executeCodeRequest);
        outputResponse.setId(executeCodeRequest.getId());

        if(result==100){
            homeMapper.updateCommitAndAccepted(executeCodeRequest.getTopicId());
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", BaseContext.getCurrentId()); // 设置更新条件，这里假设是根据id来更新
            updateWrapper.setSql("accepted = accepted + 1"); // 设置count字段自增1
            userMapper.update(null, updateWrapper); // 执行更新操作，第一个参数为null表示不使用实体对象进行更新
        }
        homeMapper.updateCommit(executeCodeRequest.getTopicId());

        return outputResponse;
    }

    /**
     * 编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile)
    {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try
        {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            // 编译失败
            if (executeMessage.getExitValue() != 0)
            {
                executeMessage.setExitValue(1);
                executeMessage.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getText());
                executeMessage.setErrorMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            }
            return executeMessage;
        }
        catch (Exception e)
        {
            // 未知错误
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setExitValue(1);
            executeMessage.setMessage(e.getMessage());
            executeMessage.setErrorMessage(JudgeInfoMessageEnum.SYSTEM_ERROR.getValue());
            return executeMessage;
        }
    }

    /**
     * 执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList)
    {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList)
        {
            // 安全控制：限制资源分配：最大队资源大小：128MB
            // 安全控制：配置安全管理器：java.lang.SecurityManager
//     不知道为什么报错 String runCmd = String.format("java -Xmx128m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            String runCmd = String.format("java -Xmx128m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            System.out.println(runCmd);
            String osName = System.getProperty("os.name").toLowerCase();
            // 如果是Windows系统，支持安全管理器security-manager的创建，反之是Linux则不支持（可能也支持，但作者暂时因时间原因未找出对策，故出此下策）
            if (osName.contains("nix") || osName.contains("nux"))
            {
                runCmd = String.format("java -Xmx128m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            }
            // String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try
            {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 安全控制：限制最大运行时间，超时控制
                new Thread(() ->
                {
                    try
                    {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超过程序最大运行时间，终止进程");
                        runProcess.destroy();
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println("本次运行结果：" + executeMessage);
                if (executeMessage.getExitValue() != 0)
                {
                    executeMessage.setExitValue(1);
                    executeMessage.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getText());
                    executeMessage.setErrorMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
                }
                executeMessageList.add(executeMessage);
            }
            catch (Exception e)
            {
                // 未知错误
                ExecuteMessage executeMessage = new ExecuteMessage();
                executeMessage.setExitValue(1);
                executeMessage.setMessage(e.getMessage());
                executeMessage.setErrorMessage(JudgeInfoMessageEnum.SYSTEM_ERROR.getValue());
                executeMessageList.add(executeMessage);
            }
        }
        return executeMessageList;
    }


}
