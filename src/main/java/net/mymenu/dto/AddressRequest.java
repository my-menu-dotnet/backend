package net.mymenu.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String street;

    @NotNull
    private String number;

    private String complement;

    @NotNull
    private String neighborhood;

    @NotNull
    private String city;

    @State
    @NotNull
    private String state;

    @CEP
    @JsonProperty("zip_code")
    @NotNull
    private String zipCode;
}
