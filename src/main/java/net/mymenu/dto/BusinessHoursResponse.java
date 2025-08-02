package net.mymenu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHoursResponse {

    private UUID id;

    @JsonProperty("day_of_week")
    private DayOfWeek dayOfWeek;

    @JsonProperty("opening_time")
    private LocalTime openingTime;

    @JsonProperty("closing_time")
    private LocalTime closingTime;

    @JsonProperty("is_closed")
    private boolean isClosed;
}
