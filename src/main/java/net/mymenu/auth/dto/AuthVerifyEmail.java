package net.mymenu.auth.dto;

import lombok.*;
import net.mymenu.auth.enums.EmailCodeType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthVerifyEmail {
    private String code;
    private EmailCodeType type;
}
