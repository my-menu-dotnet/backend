package com.digimenu.dto;

import com.digimenu.constraints.CEP;
import com.digimenu.constraints.State;
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
