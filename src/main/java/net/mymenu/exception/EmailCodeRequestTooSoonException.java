package net.mymenu.exception;

public class EmailCodeRequestTooSoonException extends RuntimeException {
    public EmailCodeRequestTooSoonException(String message) {
        super(message);
    }
}
