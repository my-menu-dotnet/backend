package com.digimenu.validators;

import com.digimenu.constraints.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    /* (11) 91234-5678
     * (011) 91234-5678
     * (11) 1234-5678
     * (11) 12345678
     * 0800 123 4567
     */

    final static private String PHONE_REGEX = "^(?:\\(?([1-9][0-9])\\)?)? ?(?:9\\d{4}|[2-9]\\d{3})-?\\d{4}$";

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return isValid(phone);
    }

    public static boolean isValid(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }
}
