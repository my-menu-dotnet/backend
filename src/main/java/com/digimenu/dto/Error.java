package com.digimenu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class Error {
    private int status;
    private String message;
    private Object errors;

    public Error(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
