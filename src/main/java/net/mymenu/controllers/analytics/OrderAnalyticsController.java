package net.mymenu.controllers.analytics;

import net.mymenu.dto.analytics.DailyOrderStatsResponse;
import net.mymenu.dto.analytics.ItemStatsResponse;
import net.mymenu.dto.analytics.MonthlyAverageTicketResponse;
import net.mymenu.dto.analytics.OrderAnalyticsResponse;
import net.mymenu.dto.analytics.OrderStatsResponse;
import net.mymenu.service.OrderAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics/orders")
public class OrderAnalyticsController {

    @Autowired
    private OrderAnalyticsService orderAnalyticsService;

    @GetMapping("/total")
    public ResponseEntity<OrderStatsResponse> getTotalOrders() {
        OrderStatsResponse response = orderAnalyticsService.getTotalOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/items-stats")
    public ResponseEntity<List<ItemStatsResponse>> getItemStats(
            @RequestParam(defaultValue = "1") Integer months) {
        List<ItemStatsResponse> response = orderAnalyticsService.getItemStatsByPeriod(months);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/daily-stats")
    public ResponseEntity<List<DailyOrderStatsResponse>> getDailyStats() {
        List<DailyOrderStatsResponse> response = orderAnalyticsService.getDailyOrdersLastMonth();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-average-ticket")
    public ResponseEntity<List<MonthlyAverageTicketResponse>> getMonthlyAverageTicket() {
        List<MonthlyAverageTicketResponse> response = orderAnalyticsService.getMonthlyAverageTicketLast12Months();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complete-analytics")
    public ResponseEntity<OrderAnalyticsResponse> getCompleteAnalytics(
            @RequestParam(required = false) Integer itemStatsPeriodMonths) {
        OrderAnalyticsResponse response = orderAnalyticsService.getOrderAnalytics(itemStatsPeriodMonths);
        return ResponseEntity.ok(response);
    }
}
