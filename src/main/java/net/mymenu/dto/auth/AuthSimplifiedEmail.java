package net.mymenu.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mymenu.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSimplifiedEmail {
    @Email
    private String email;
}
