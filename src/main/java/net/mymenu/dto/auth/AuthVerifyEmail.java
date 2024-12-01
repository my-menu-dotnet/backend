package net.mymenu.dto.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthVerifyEmail {
    private String code;
}
