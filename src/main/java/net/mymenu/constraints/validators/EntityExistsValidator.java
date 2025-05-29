package net.mymenu.constraints.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.mymenu.constraints.EntityExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EntityExistsValidator implements ConstraintValidator<EntityExists, Object> {

    private Class<? extends JpaRepository<?, UUID>> repositoryClass;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void initialize(EntityExists constraintAnnotation) {
        repositoryClass = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            JpaRepository<?, UUID> repository = applicationContext.getBean(repositoryClass);
            Optional<?> result = repository.findById(UUID.fromString(value.toString()));

            return result.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}