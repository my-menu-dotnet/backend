package net.mymenu.dto.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mymenu.enums.DiscountStatus;
import net.mymenu.enums.DiscountType;
import net.mymenu.models.Food;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
    private UUID id;

    private Food food;

    private double discount;

    private DiscountType type;

    @JsonProperty("start_at")
    private LocalDate startAt;

    @JsonProperty("end_at")
    private LocalDate endAt;

    private DiscountStatus status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
