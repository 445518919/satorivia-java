package com.satoriviacafe.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shy
 * @since 2025年06月02日
 * 扩展 MVC 配置：注册 RequestContextListener 以支持跨线程请求上下文传播
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 让 RequestContextHolder 在异步线程或虚拟线程里也能拿到当前请求的 RequestAttributes
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
