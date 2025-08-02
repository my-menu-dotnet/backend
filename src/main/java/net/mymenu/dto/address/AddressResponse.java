package net.mymenu.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressResponse {
    private String street;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

    private String zipCode;

    private String number;
}
