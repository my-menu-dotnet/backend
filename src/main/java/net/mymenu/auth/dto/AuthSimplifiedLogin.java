package net.mymenu.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthSimplifiedLogin {
    private String email;
    private String code;
}
