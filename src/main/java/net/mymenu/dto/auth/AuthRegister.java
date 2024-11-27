package net.mymenu.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.mymenu.constraints.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthRegister {

    @FullName
    private String name;

    @Email
    private String email;

    @CPF
    private String cpf;

    @Phone
    private String phone;

    @Password
    private String password;
}
