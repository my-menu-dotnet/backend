package net.mymenu.exception;

import net.mymenu.dto.Error;
import net.mymenu.service.CookieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private CookieService cookieService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getField(), v.getDefaultMessage()), HashMap::putAll);

        Error error = Error
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid request")
                .data(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Error> handleSecurityException(SecurityException e) {
        e.printStackTrace();

        ResponseCookie cookieRefreshToken = cookieService.createCookie("refreshToken", "", 0, "/auth");
        ResponseCookie cookieAccessToken = cookieService.createCookie("accessToken", "", 0, "/");


        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString())
                .build();
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<Error> handleInternalErrorException(InternalErrorException e) {
        LOGGER.error("Internal error", e);

        Error error = Error
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid request")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        LOGGER.error("Unexpected error", e);

        String message = e.getMessage() != null ? e.getMessage() : "Invalid request";
        HttpStatus status = e.getHttpStatus();

        Error error = Error
                .builder()
                .status(status.value())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(error);
    }
}
