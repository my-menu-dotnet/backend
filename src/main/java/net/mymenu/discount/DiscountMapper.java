package net.mymenu.discount;

import net.mymenu.discount.dto.DiscountDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    DiscountDTO toDiscountDTO(Discount food);

    List<DiscountDTO> toDiscountDTO(List<Discount> food);
}
