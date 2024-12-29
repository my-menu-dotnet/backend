package net.mymenu.mapper;

import net.mymenu.dto.discount.DiscountDTO;
import net.mymenu.models.Discount;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    DiscountDTO toDiscountDTO(Discount food);

    List<DiscountDTO> toDiscountDTO(List<Discount> food);
}
