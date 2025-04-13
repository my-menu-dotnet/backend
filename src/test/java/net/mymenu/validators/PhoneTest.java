package net.mymenu.validators;

import net.mymenu.constraints.validators.PhoneValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneTest {
    /*
     * Testa um telefone válido.
     *
     * (11) 91234-5678
     * (011) 91234-5678
     * (11) 1234-5678
     * 0800 123 4567
     */

    @Test
    @Disabled
    public void testValidPhone() {
        String phone = "(11) 91234-5678";
        boolean isValid = PhoneValidator.isValid(phone);
        assertTrue(isValid);
    }

    @Test
    @Disabled
    public void testValidPhoneWithDDD() {
        String phone = "(011) 91234-5678";
        boolean isValid = PhoneValidator.isValid(phone);
        assertTrue(isValid);
    }

    @Test
    @Disabled
    public void testValidPhoneWithoutHyphen() {
        String phone = "(11) 12345678";
        boolean isValid = PhoneValidator.isValid(phone);
        assertTrue(isValid);
    }

    @Test
    @Disabled
    public void testValidPhone0800() {
        String phone = "0800 123 4567";
        boolean isValid = PhoneValidator.isValid(phone);
        assertTrue(isValid);
    }

    @Test
    @Disabled
    public void testInvalidPhoneWithSpaces() {
        String phone = " (11) 91234-5678 ";
        boolean isValid = PhoneValidator.isValid(phone);
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testInvalidPhoneWithLessCharacters() {
        String phone = "(11) 1234-567";
        boolean isValid = PhoneValidator.isValid(phone);
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testInvalidPhoneWithoutDDD() {
        String phone = "91234-5678";
        boolean isValid = PhoneValidator.isValid(phone);
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testInvalidPhoneWithoutNumbers() {
        String phone = "(11) 1234-ABCD";
        boolean isValid = PhoneValidator.isValid(phone);
        assertFalse(isValid);
    }

    @Test
    @Disabled
    public void testInvalidPhoneWithLetters() {
        String phone = "(11) 1234-ABCD";
        boolean isValid = PhoneValidator.isValid(phone);
        assertFalse(isValid);
    }
}
