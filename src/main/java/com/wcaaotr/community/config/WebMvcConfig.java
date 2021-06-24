package com.wcaaotr.community.config;

import com.wcaaotr.community.interceptor.LoginRequiredInterceptor;
import com.wcaaotr.community.interceptor.LoginTicketInterceptor;
import com.wcaaotr.community.interceptor.MessageInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Connor
 * @create 2021-06-21-22:02
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInteceptor messageInteceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInteceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.jpeg");
    }
}
