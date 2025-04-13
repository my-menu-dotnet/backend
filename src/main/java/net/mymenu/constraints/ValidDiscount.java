package net.mymenu.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.mymenu.constraints.validators.DiscountValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DiscountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscount {
    String message() default "Invalid discount value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}