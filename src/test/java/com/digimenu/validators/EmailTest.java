package com.digimenu.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailTest {

    @Test
    public void testValidEmail() {
        String email = "teste@teste.com";
        boolean isValid = EmailValidator.isValid(email);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidEmail() {
        String email = "teste";
        boolean isValid = EmailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailWithoutAt() {
        String email = "teste.com";
        boolean isValid = EmailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailWithoutDomain() {
        String email = "teste@";
        boolean isValid = EmailValidator.isValid(email);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailWithoutUser() {
        String email = "@teste.com";
        boolean isValid = EmailValidator.isValid(email);
        assertFalse(isValid);
    }
}
