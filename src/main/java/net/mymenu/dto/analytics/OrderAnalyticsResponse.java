package net.mymenu.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAnalyticsResponse {
    private long totalOrders;
    private List<ItemStatsResponse> itemStats;
    private List<DailyOrderStatsResponse> dailyStats;
    private List<MonthlyAverageTicketResponse> monthlyAverageTicket;
}
