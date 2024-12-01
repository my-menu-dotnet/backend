package net.mymenu.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {
    private int status;
    private String message;
    private Object errors;

    public Error(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
