package net.mymenu.dto.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import net.mymenu.constraints.ValidDateRange;
import net.mymenu.constraints.ValidDiscount;
import net.mymenu.enums.DiscountType;
import net.mymenu.enums.DiscountStatus;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidDateRange
@ValidDiscount
public class DiscountRequest {

    @JsonProperty("food_id")
    private UUID foodId;

    @JsonProperty("discount")
    @Positive(message = "The discount must be greater than zero")
    private double discount;

    @JsonProperty("type")
    @NotNull
    private DiscountType type;

    @NotNull
    private boolean active;

    @JsonProperty("start_at")
    @FutureOrPresent(message = "The start date must be in the present or future")
    private LocalDate startAt;

    @JsonProperty("end_at")
    @FutureOrPresent(message = "The end date must be in the present or future")
    private LocalDate endAt;
}
