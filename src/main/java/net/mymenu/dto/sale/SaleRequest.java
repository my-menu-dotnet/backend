package net.mymenu.dto.sale;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.mymenu.enums.SaleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {

    @JsonProperty("food_id")
    private UUID foodId;

    @JsonProperty("discount")
    private double discount;

    @JsonProperty("start_at")
    private LocalDateTime startAt;

    @JsonProperty("end_at")
    private LocalDateTime endAt;

    @JsonProperty("status")
    private SaleStatus status;
}
