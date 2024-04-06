package com.MuffinHead.service.service.python3;

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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Python3 代码沙箱模板方法的实现
 *
 * @author zzx
 */
@Slf4j
public abstract class Python3CodeSandboxTemplate extends CommonCodeSandboxTemplate implements CodeSandbox
{

    /**
     * 待运行代码的文件夹路径名称
     */
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    /**
     * 待运行代码的存放文件名
     */
    private static final String GLOBAL_PYTHON_FILE_NAME = "Main.py";

    /**
     * 代码最大允许运行的时间
     */
    private static final long TIME_OUT = 10000L;

    /**
     * 设置Python最大可用内存为128MB
     */
    private static final String MEMORY_LIMIT_PREFIX_CODE = "import resource;max_memory = 128;resource.setrlimit(resource.RLIMIT_AS, (max_memory * (1024 ** 2), -1));";

    /**
     * Python代码黑名单
     * 黑名单检测通常用于辅助安全策略，而不是作为唯一的安全手段
     */
    private static final List<String> blackList = Arrays.asList(
            // 文件操作相关
            "open", "os.system", "os.popen", "os.fdopen", "shutil.copy", "shutil.move", "shutil.rmtree",

            // 网络相关
            "socket", "http.client.HTTPConnection", "http.client.HTTPSConnection", "urllib.request.urlopen", "urllib.request.urlretrieve",

            // 系统命令执行相关
            "subprocess.run", "subprocess.Popen",

            // 反射相关
            "__import__", "eval", "exec",

            // 数据库相关
            "sqlite3", "MySQLdb",

            // 加密解密相关
            "cryptography",

            // 序列化相关
            "pickle",

            // 线程相关
            "threading.Thread", "multiprocessing.Process",

            // 安全管理器相关
            "java.lang.SecurityManager",

            // 其他可能导致安全问题的操作
            "ctypes.CDLL", "ctypes.WinDLL", "ctypes.CFUNCTYPE", "os.environ", "os.putenv", "atexit.register",

            // 与操作系统交互
            "os.chmod", "os.chown",

            // 文件权限控制
            "os.access", "os.setuid", "os.setgid",

            // 环境变量操作
            "os.environ['SOME_VAR']", "os.putenv('SOME_VAR', 'value')",

            // 不安全的输入
            "input", "raw_input",

            // 不安全的字符串拼接
            "eval(f'expr {var}')", "var = var1 + var2",

            // 定时器相关
            "time.sleep",

            // 定时任务
            "schedule",

            // 本地文件包含
            "exec(open('filename').read())",

            // 不安全的网站访问
            "urllib.urlopen",

            // 系统退出
            "exit",

            // 其他危险操作
            "os.remove", "os.unlink", "os.rmdir", "os.removedirs", "os.rename", "os.execvp", "os.execlp",

            // 不安全的随机数生成
            "random",

            // 不安全的正则表达式
            "re.compile",

            // 使用 eval 解析 JSON
            "eval(json_string)",

            // 使用 pickle 处理不受信任的数据
            "pickle.loads",

            // 使用 exec 执行不受信任的代码
            "exec(code)",

            // 不安全的 HTML 解析
            "BeautifulSoup",

            // 不安全的 XML 解析
            "xml.etree.ElementTree",

            // 使用自定义反序列化
            "pickle.Unpickler", "marshal.loads",

            // 在代码中直接拼接 SQL
            "sqlalchemy.text",

            // 不安全的图像处理
            "PIL.Image",

            // 使用 ctypes 执行外部 C 代码
            "ctypes.CDLL",

            // 不安全的邮件操作
            "smtplib", "poplib",

            // 不安全的 URL 拼接
            "urllib.parse.urljoin",

            // 使用 eval 执行 JavaScript 代码
            "execjs.eval",

            // 不安全的 Web 框架设置
            "Flask.app.secret_key",

            // 不安全的 API 请求
            "requests.get", "requests.post",

            // 不安全的模板引擎
            "Jinja2.Template",

            // 不安全的数据反序列化
            "pickle.loads", "marshal.loads",

            // 不安全的文件上传
            "werkzeug.FileStorage",

            // 不安全的命令行参数解析
            "argparse.ArgumentParser");

    /**
     * 代码黑名单字典树
     */
    private static final WordTree WORD_TREE;

    @Autowired
    private HomeMapper homeMapper;
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private UserMapper userMapper;

    static
    {
        // 初始化黑名单字典树
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blackList);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        String[] inputList2 = executeCodeRequest.getInput().split("\\+");
        List<String> inputList = new ArrayList<>(Arrays.asList(inputList2));
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
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

        // 安全控制：限制资源分配：最大队资源大小：128MB
        // 判断所处的操作系统，如果是Windows系统，则无法使用resource的Python库和限制内存大小，因此不建议在Windows系统上运行代码。
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("nix") || osName.contains("nux"))
        {
            code = MEMORY_LIMIT_PREFIX_CODE + "\r\n" + code;
            System.out.println("安全控制后的代码：\n" + code);
        }
        // 1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code, GLOBAL_CODE_DIR_NAME, GLOBAL_CODE_DIR_NAME);

        // 2. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);

        // 3. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList);


        //运行错误
        if(outputResponse.getMessage().equals("Runtime Error")){
            boolean b = deleteFile(userCodeFile);
            if (!b)
            {
                log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
            }
            executeCodeRequest.setResult(-4);
            executeCodeRequest.setExecutionTime(Math.toIntExact(outputResponse.getJudgeInfo().getTime()));
            executeCodeRequest.setMemory(Math.toIntExact(outputResponse.getJudgeInfo().getMemory()));
            executeCodeRequest.setSubmitTime(new Date());
            codeMapper.save(executeCodeRequest);
            homeMapper.updateCommit(executeCodeRequest.getTopicId());
            return new ExecuteCodeResponse(null, outputResponse.getMessage(), QuestionSubmitStatusEnum.FAILED.getValue(), new JudgeInfo("运行错误", outputResponse.getJudgeInfo().getMemory(), outputResponse.getJudgeInfo().getTime()),executeCodeRequest.getId());

        }

        // 4. 文件清理
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
     * 执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList)
    {
        String userCodeFileAbsolutePath = userCodeFile.getAbsolutePath();
        // 判断操作系统，Windows使用Python，而Linux使用Python3
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println("当前操作系统：" + osName);
        String pythonCmdPrefix = "python3";
        if (osName.contains("win"))
        {
            pythonCmdPrefix = "python";
        }
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList)
        {
            String runCmd = String.format("%s %s %s", pythonCmdPrefix, userCodeFileAbsolutePath, inputArgs);
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
