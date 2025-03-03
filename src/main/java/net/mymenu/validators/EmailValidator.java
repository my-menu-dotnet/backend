package net.mymenu.validators;

import net.mymenu.constraints.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private final static String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return EmailValidator.isValid(email);
    }

    public static boolean isValid(String email) {
        if (email == null) {
            return true;
        }
        return email.matches(EMAIL_REGEX);
    }
}
