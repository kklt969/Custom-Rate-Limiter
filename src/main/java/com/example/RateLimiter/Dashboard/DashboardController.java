package com.example.RateLimiter.Dashboard;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardMetricsService dashboardMetricsService;

    public DashboardController(DashboardMetricsService dashboardMetricsService) {
        this.dashboardMetricsService = dashboardMetricsService;
    }

    @GetMapping("/metrics")
    public DashboardMetrics getDashboardMetrics() {

        return new DashboardMetrics(dashboardMetricsService.getTotalRequests(), dashboardMetricsService.getAllowedRequests(), dashboardMetricsService.getRejectedRequests(), dashboardMetricsService.getCacheHits(), dashboardMetricsService.getCacheMisses());

    }


}
