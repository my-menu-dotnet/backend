package com.digimenu.dto.menu;

import com.digimenu.models.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MenuDTO {
    Company company;
    List<MenuCategoryDTO> categories;
}
