package net.mymenu.validators;

import net.mymenu.constraints.validators.PasswordValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordTest {

    /**
     * Testa uma senha válida.
     * 8 caracteres, pelo menos 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial.
     */

    @Test
    public void testValidPassword() {
        String password = "Teste@123";
        boolean isValid = PasswordValidator.isValid(password);
        assertTrue(isValid);
    }

    @Test
    public void testValidPasswordWithSpecialCharacters() {
        String password = "Teste@123";
        boolean isValid = PasswordValidator.isValid(password);
        assertTrue(isValid);
    }

    @Test
    public void testValidPasswordWithHyphen() {
        String password = "Teste-123";
        boolean isValid = PasswordValidator.isValid(password);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidPasswordWithSpaces() {
        String password = " Teste@123 ";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordWithLessCharacters() {
        String password = "Teste@1";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordWithoutSpecialCharacters() {
        String password = "Teste123";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordWithoutNumbers() {
        String password = "Teste@";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordWithoutUppercaseLetters() {
        String password = "teste@123";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidPasswordWithoutLowercaseLetters() {
        String password = "TESTE@123";
        boolean isValid = PasswordValidator.isValid(password);
        assertFalse(isValid);
    }

}
