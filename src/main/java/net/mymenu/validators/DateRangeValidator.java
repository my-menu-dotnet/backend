package net.mymenu.validators;

import net.mymenu.constraints.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.mymenu.constraints.ValidDateRange;
import net.mymenu.dto.discount.DiscountRequest;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DiscountRequest> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(DiscountRequest discountRequest, ConstraintValidatorContext context) {
        if (discountRequest.getStartAt() == null || discountRequest.getEndAt() == null) {
            return true;
        }
        return !discountRequest.getStartAt().isAfter(discountRequest.getEndAt());
    }

}
