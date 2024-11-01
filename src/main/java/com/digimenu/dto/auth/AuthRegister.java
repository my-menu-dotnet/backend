package com.digimenu.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRegister {
    private String name;
    private String email;
    private String cpf;
    private String phone;
    private String password;
}
