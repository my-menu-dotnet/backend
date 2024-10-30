package com.digimenu.dto;

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
