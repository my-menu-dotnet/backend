package net.mymenu.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyOrderStatsResponse {
    private LocalDate date;
    private Long totalOrders;


    // Construtor para java.sql.Date e Long
    public DailyOrderStatsResponse(LocalDateTime date, Long totalOrders) {
        this.date = date != null ? date.toLocalDate() : null;
        this.totalOrders = totalOrders;
    }

    // Construtor para Object (caso a função DATE retorne outro tipo) e Long
    public DailyOrderStatsResponse(Object dateObj, Long totalOrders) {
        if (dateObj instanceof java.sql.Date) {
            this.date = ((java.sql.Date) dateObj).toLocalDate();
        } else if (dateObj instanceof java.util.Date) {
            this.date = ((java.util.Date) dateObj).toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
        } else if (dateObj instanceof LocalDate) {
            this.date = (LocalDate) dateObj;
        } else if (dateObj instanceof String) {
            this.date = LocalDate.parse(dateObj.toString());
        }
        this.totalOrders = totalOrders;
    }

    // Construtor para casos onde COUNT retorna Integer ao invés de Long
    public DailyOrderStatsResponse(Object dateObj, Integer totalOrdersInt) {
        this(dateObj, totalOrdersInt != null ? totalOrdersInt.longValue() : 0L);
    }
}
