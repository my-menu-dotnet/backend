package net.mymenu.constraints.validators;

import net.mymenu.constraints.CEP;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CEPValidator implements ConstraintValidator<CEP, String> {

    @Override
    public void initialize(CEP constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        return CEPValidator.isValid(cep);
    }

    public static boolean isValid(String cep) {
        return cep != null && cep.matches("^[0-9]{5}[0-9]{3}$");
    }
}
