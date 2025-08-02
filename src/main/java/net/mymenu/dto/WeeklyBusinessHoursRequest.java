package net.mymenu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyBusinessHoursRequest {

    @Valid
    @Size(min = 1, max = 7, message = "Deve conter horários para pelo menos 1 dia e no máximo 7 dias")
    @JsonProperty("business_hours")
    private List<BusinessHoursRequest> businessHours;
}
