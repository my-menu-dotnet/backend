package net.mymenu.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.mymenu.constraints.*;
import net.mymenu.dto.AddressRequest;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserRequest {

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

    @Valid
    private AddressRequest address;
}
