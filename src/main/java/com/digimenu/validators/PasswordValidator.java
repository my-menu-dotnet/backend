package com.digimenu.validators;

import com.digimenu.constraints.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    /*
     * The password must have:
     * 8 characters, at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.
     */
    private final static String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return isValid(password);
    }

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }
}
