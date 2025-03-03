package net.mymenu.validators;

import net.mymenu.constraints.CPF;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public void initialize(CPF constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return CPFValidator.isValid(cpf);
    }

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return true;
        }

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }

            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += digits[i] * (10 - i);
            }
            int firstVerifier = 11 - (sum1 % 11);
            if (firstVerifier >= 10) {
                firstVerifier = 0;
            }

            if (digits[9] != firstVerifier) {
                return false;
            }

            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += digits[i] * (11 - i);
            }
            int secondVerifier = 11 - (sum2 % 11);
            if (secondVerifier >= 10) {
                secondVerifier = 0;
            }

            return digits[10] == secondVerifier;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
