package net.mymenu.exception;

public class EmailCodeInvalidException extends MyMenuException {
    public EmailCodeInvalidException() {
        super("Invalid email code");
    }
}
