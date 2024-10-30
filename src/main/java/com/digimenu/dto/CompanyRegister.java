package com.digimenu.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegister {

    @NotBlank
    private String name;

    @NotBlank
    private String cnpj;

    @NotBlank
    @Email
    private String email;

    private String phone;

}
