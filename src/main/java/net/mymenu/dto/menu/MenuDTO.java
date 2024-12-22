package net.mymenu.dto.menu;

import net.mymenu.models.Category;
import net.mymenu.models.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MenuDTO {
    Company company;
    List<Category> categories;
}
