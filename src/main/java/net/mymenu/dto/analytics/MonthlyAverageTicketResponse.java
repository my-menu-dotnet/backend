package net.mymenu.dto.analytics;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
public class MonthlyAverageTicketResponse {
    private Integer year;
    private Integer month;
    private BigDecimal averageTicket;

    // Construtor para query nativa - year, month, average
    public MonthlyAverageTicketResponse(Integer year, Integer month, Double averageTicket) {
        this.year = year;
        this.month = month;
        this.averageTicket = averageTicket != null ? BigDecimal.valueOf(averageTicket) : BigDecimal.ZERO;
    }

    // Construtor completo para Builder
    public MonthlyAverageTicketResponse(Integer year, Integer month, BigDecimal averageTicket) {
        this.year = year;
        this.month = month;
        this.averageTicket = averageTicket != null ? averageTicket : BigDecimal.ZERO;
    }
}
