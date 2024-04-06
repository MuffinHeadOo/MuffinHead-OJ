package com.MuffinHead.service.config;

import com.MuffinHead.service.interceptor.CrossInterceptor;
import com.MuffinHead.service.interceptor.JwtTokenUserInterceptor;
import com.MuffinHead.service.interceptor.OptionsInterceptor;
import com.MuffinHead.service.properties.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param
     */
    @Override
    public  void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");

        registry.addInterceptor(new CrossInterceptor()).addPathPatterns("/**");
        log.info("CrossInterceptor拦截器注册成功！");

        registry.addInterceptor(new OptionsInterceptor()).addPathPatterns("/**");
        log.info("Options请求拦截器注册成功！");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/home/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/admin/**")
                .addPathPatterns("/codesandbox/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/loginAdmin")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/forget");

    }

}
