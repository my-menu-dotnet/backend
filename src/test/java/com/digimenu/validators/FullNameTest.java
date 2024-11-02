package com.digimenu.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullNameTest {

    @Test
    public void testValidFullName() {
        String fullName = "John Doe";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithSpecialCharacters() {
        String fullName = "João da Silva";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithHyphen() {
        String fullName = "John-Doe";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithApostrophe() {
        String fullName = "John O'Doe";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithSpaces() {
        String fullName = " John Doe ";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testValidFullNameWithNumbers() {
        String fullName = "John Doe 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testValidFullNameWithSpecialCharactersAndNumbers() {
        String fullName = "João da Silva 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testValidFullNameWithHyphenAndNumbers() {
        String fullName = "John-Doe 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testValidFullNameWithApostropheAndNumbers() {
        String fullName = "John O'Doe 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testValidFullNameWithSpecialCharactersAndHyphen() {
        String fullName = "João da-Silva";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithSpecialCharactersAndApostrophe() {
        String fullName = "João O'Da Silva";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithHyphenAndApostrophe() {
        String fullName = "John-Doe O'Doe";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testValidFullNameWithSpecialCharactersAndHyphenAndApostrophe() {
        String fullName = "João da-Silva O'Da Silva";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidFullName() {
        String fullName = "John Doe 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidFullNameWithSpecialCharacters() {
        String fullName = "João da Silva 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidFullNameWithHyphen() {
        String fullName = "John-Doe 123";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }

    @Test
    public void testWithOneWord() {
        String fullName = "John";
        boolean isValid = FullNameValidator.isValid(fullName);
        assertFalse(isValid);
    }
}
