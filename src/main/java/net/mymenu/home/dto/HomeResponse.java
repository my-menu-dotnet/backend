package net.mymenu.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponse {

    private long totalCompanies;

    private long totalFood;

    private long totalAccess;
}
