package net.mymenu.dto.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import net.mymenu.constraints.CEP;
import net.mymenu.constraints.CPF;
import net.mymenu.constraints.Email;
import net.mymenu.constraints.Phone;
import net.mymenu.constraints.State;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @Phone
    private String phone;

    @CPF
    private String cpf;

    @Valid
    private AddressPayload address;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressPayload {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;

        @State
        private String state;

        @CEP
        private String zipCode;
    }
}
