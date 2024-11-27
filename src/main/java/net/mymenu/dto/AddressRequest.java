package net.mymenu.dto;

import net.mymenu.constraints.CEP;
import net.mymenu.constraints.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressRequest {

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    @State
    private String state;

    @CEP
    @JsonProperty("zip_code")
    private String zipCode;
}
