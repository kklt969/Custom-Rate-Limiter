package com.example.RateLimiter;

import com.example.RateLimiter.Redis.RedisRateLimiterService;
import org.apache.catalina.util.RateLimiter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //private final RateLimiterService rateLimiterService;

    private final RedisRateLimiterService rateLimiterService;

    public WebConfig(RedisRateLimiterService  rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(new RateLimitInterceptor(rateLimiterService));
    }




}
