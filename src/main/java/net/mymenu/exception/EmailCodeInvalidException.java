package net.mymenu.exception;

public class EmailCodeInvalidException extends Exception {
    public EmailCodeInvalidException() {
        super("Invalid email code");
    }
}
