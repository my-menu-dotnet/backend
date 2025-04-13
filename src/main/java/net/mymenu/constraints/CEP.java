package net.mymenu.constraints;

import net.mymenu.constraints.validators.CEPValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CEPValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CEP {
    String message() default "Invalid CEP";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
