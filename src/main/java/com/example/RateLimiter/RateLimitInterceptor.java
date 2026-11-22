package com.example.RateLimiter;

import com.example.RateLimiter.Dashboard.DashboardMetricsService;
import com.example.RateLimiter.Redis.RedisRateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.atomic.AtomicLong;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisRateLimiterService rateLimiterService;

    private boolean rateLimitingEnabled = true;
    private DashboardMetricsService dashboardMetricsService;

    public RateLimitInterceptor(RedisRateLimiterService rateLimiterService, DashboardMetricsService dashboardMetricsService) {
        this.rateLimiterService = rateLimiterService;
        this.dashboardMetricsService = dashboardMetricsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{


        String clientId = request.getHeader("X-API-Key");

        if(clientId == null || clientId.isBlank()) {

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");

            response.getWriter().write("""
                    
                    {
                           "error": "Bad Request",
                            "message": "X-API-Key header is required."
                    }c
                    
                    """);

            return false;
        }

        dashboardMetricsService.recordRequest();



        if(!rateLimiterService.allow(clientId)) {

            response.setContentType("application/json");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", "1");

            response.getWriter().write("""
        {
            "error": "Too Many Requests",
            "message": "Rate limit exceeded. Try again later."
        }
        """);

            dashboardMetricsService.recordRejectedRequest();

        return false;
        }

        dashboardMetricsService.recordAllowedRequest();
        return true;

    }


}
