package net.mymenu.dto.auth;

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
