package net.mymenu.constraints;

import net.mymenu.constraints.validators.StateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
    String message() default "Invalid State";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
