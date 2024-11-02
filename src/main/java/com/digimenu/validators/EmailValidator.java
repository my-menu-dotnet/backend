package com.digimenu.validators;

import com.digimenu.constraints.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private final static String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return CPFValidator.isValid(email);
    }

    public static boolean isValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
