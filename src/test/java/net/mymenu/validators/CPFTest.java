package net.mymenu.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CPFTest {

    @Test
    public void testValidCPF() {
        String cpf = "90828187010";
        boolean isValid = CPFValidator.isValid(cpf);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidCPF() {
        String cpf = "00000000000";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidCPFWithRepeatedDigits() {
        String cpf = "11111111111";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidCPFWithLessDigits() {
        String cpf = "1234567890";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidCPFWithMoreDigits() {
        String cpf = "123456789012";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidCPFWithLetters() {
        String cpf = "1234567890a";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testValidWithSpaces() {
        String cpf = " 90828187010 ";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

    @Test
    public void testWithMask() {
        String cpf = "908.281.870-10";
        boolean isValid = CPFValidator.isValid(cpf);
        assertFalse(isValid);
    }

}
