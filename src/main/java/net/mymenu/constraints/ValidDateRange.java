package net.mymenu.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.mymenu.constraints.validators.DateRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "The start date must be before the end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

