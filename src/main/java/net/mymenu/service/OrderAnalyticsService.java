package net.mymenu.service;

import net.mymenu.dto.analytics.DailyOrderStatsResponse;
import net.mymenu.dto.analytics.ItemStatsResponse;
import net.mymenu.dto.analytics.OrderAnalyticsResponse;
import net.mymenu.dto.analytics.OrderStatsResponse;
import net.mymenu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderAnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderStatsResponse getTotalOrders() {
        long totalOrders = orderRepository.count();
        return OrderStatsResponse.builder()
                .totalOrders(totalOrders)
                .build();
    }

    public List<ItemStatsResponse> getItemStatsByPeriod(int months) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(months);

        return orderRepository.getItemStatsByDateRange(startDate, endDate);
    }

    public List<DailyOrderStatsResponse> getDailyOrdersLastMonth() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        return orderRepository.getDailyOrderStatsByDateRange(startDate, endDate);
    }

    public OrderAnalyticsResponse getOrderAnalytics(Integer itemStatsPeriodMonths) {
        int periodMonths = itemStatsPeriodMonths != null ? itemStatsPeriodMonths : 1;

        return OrderAnalyticsResponse.builder()
                .totalOrders(getTotalOrders().getTotalOrders())
                .itemStats(getItemStatsByPeriod(periodMonths))
                .dailyStats(getDailyOrdersLastMonth())
                .build();
    }
}
