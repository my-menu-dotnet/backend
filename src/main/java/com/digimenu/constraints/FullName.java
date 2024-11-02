package com.digimenu.constraints;

import com.digimenu.validators.FullNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FullNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullName {
    String message() default "Invalid Full Name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
