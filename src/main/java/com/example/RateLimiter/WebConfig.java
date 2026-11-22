package com.example.RateLimiter;

import com.example.RateLimiter.Dashboard.DashboardMetricsService;
import com.example.RateLimiter.Redis.RedisRateLimiterService;
import org.apache.catalina.util.RateLimiter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //private final RateLimiterService rateLimiterService;

    private final RedisRateLimiterService rateLimiterService;

    private DashboardMetricsService dashboardMetricsService;

    public WebConfig(RedisRateLimiterService  rateLimiterService, DashboardMetricsService dashboardMetricsService) {

        this.rateLimiterService = rateLimiterService;
        this.dashboardMetricsService = dashboardMetricsService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){

        registry.addInterceptor(new RateLimitInterceptor(rateLimiterService, dashboardMetricsService)).excludePathPatterns("/metrics");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }




}
