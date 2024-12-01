package net.mymenu.exception;

public class EmailCodeExpiredException extends RuntimeException {
    public EmailCodeExpiredException() {
        super("Email code expired");
    }
}
