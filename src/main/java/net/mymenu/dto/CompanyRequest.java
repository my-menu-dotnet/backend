package net.mymenu.dto;

import jakarta.validation.Valid;
import net.mymenu.constraints.Phone;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    @NotBlank
    private String name;

    private String cnpj;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Phone
    private String phone;

    @JsonProperty("categories")
    private List<UUID> categories;

    @JsonProperty("image_id")
    private UUID imageId;

    @Valid
    private AddressRequest address;
}
