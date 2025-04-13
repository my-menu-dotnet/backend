package net.mymenu.constraints.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.mymenu.constraints.ValidDiscount;
import net.mymenu.discount.dto.DiscountRequest;
import net.mymenu.discount.enums.DiscountType;

public class DiscountValidator implements ConstraintValidator<ValidDiscount, DiscountRequest> {

    @Override
    public void initialize(ValidDiscount constraintAnnotation) {
    }

    @Override
    public boolean isValid(DiscountRequest discountRequest, ConstraintValidatorContext context) {
        if (discountRequest.getType() == DiscountType.PERCENTAGE) {
            return discountRequest.getDiscount() <= 100;
        }
        return true;
    }

}