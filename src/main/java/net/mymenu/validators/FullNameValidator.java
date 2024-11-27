package net.mymenu.validators;

import net.mymenu.constraints.FullName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FullNameValidator implements ConstraintValidator<FullName, String> {

    final private static String FULL_NAME_REGEX = "^[\\p{L}']+([\\s-][\\p{L}']+)+$";

    @Override
    public void initialize(FullName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return isValid(phone);
    }

    public static boolean isValid(String fullName) {
        return fullName != null && fullName.matches(FULL_NAME_REGEX);
    }
}
