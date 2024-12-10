package net.mymenu.dto.auth;

import lombok.*;
import net.mymenu.enums.auth.EmailCodeType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthVerifyEmail {
    private String code;
    private EmailCodeType type;
}
