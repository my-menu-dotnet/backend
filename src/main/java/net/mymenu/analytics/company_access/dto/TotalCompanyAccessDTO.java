package net.mymenu.analytics.company_access.dto;

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