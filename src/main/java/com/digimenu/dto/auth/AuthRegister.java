package com.digimenu.dto.auth;

import com.digimenu.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
