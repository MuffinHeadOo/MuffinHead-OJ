package com.MuffinHead.service.interceptor;


import com.MuffinHead.common.context.BaseContext;
import com.MuffinHead.common.exception.CustomException;
import com.MuffinHead.model.common.enums.AppHttpCodeEnum;
import com.MuffinHead.service.utils.AppJwtUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {



    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("进入拦截器");
        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        token = token.replace("\"", "");

        //3.判断token是否存在
        if(StringUtils.isBlank(token)){
            response.setStatus(401);
            return false;
        }

        //4.判断token是否有效
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            //检验是否过期
            int result = AppJwtUtil.verifyToken(claimsBody);

            if(result == 1 || result == 2){
                throw new CustomException(AppHttpCodeEnum.TOKEN_EXPIRE);
            }
            //解析token获取用户信息
            Object userId = claimsBody.get("id");

            BaseContext.setCurrentId(Long.valueOf(String.valueOf(userId)));
            //3、通过，放行
            return true;
        }  catch (Exception e){
            e.printStackTrace(); // 打印异常的堆栈轨迹到标准错误流
            System.out.println("异常类型: " + e.getClass().getName()); // 获取异常类型
            System.out.println("异常消息: " + e.getMessage()); // 获取异常消息
            response.setStatus(401);
            return false;
        }

    }
}
