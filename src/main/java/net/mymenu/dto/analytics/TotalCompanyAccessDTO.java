package net.mymenu.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class TotalCompanyAccessDTO {
    private int totalAccess;
    private double growthPercentage;
}