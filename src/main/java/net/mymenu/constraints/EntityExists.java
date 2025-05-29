package net.mymenu.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.mymenu.constraints.validators.EntityExistsValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Constraint(validatedBy = EntityExistsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityExists {
    String message() default "Entity with this ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends JpaRepository<?, UUID>> repository();
}
