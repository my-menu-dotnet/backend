package net.mymenu.validators;

import net.mymenu.constraints.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class StateValidator implements ConstraintValidator<State, String> {

    private final static List<String> STATES = List.of(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    @Override
    public void initialize(State constraintAnnotation) {
    }

    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        return StateValidator.isValid(state);
    }

    public static boolean isValid(String state) {
        return state != null && STATES.contains(state);
    }
}
