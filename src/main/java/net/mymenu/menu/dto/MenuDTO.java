package net.mymenu.menu.dto;

import net.mymenu.banner.Banner;
import net.mymenu.category.Category;
import net.mymenu.company.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MenuDTO {
    Company company;
    List<Banner> banners;
    List<Category> categories;
}
