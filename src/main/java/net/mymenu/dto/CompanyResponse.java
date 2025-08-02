package net.mymenu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.mymenu.dto.address.AddressResponse;
import net.mymenu.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    private UUID id;
    private String name;
    private String description;
    private String cnpj;
    private String phone;
    private String email;
    private String url;

    @JsonProperty("primary_color")
    private String primaryColor;

    @JsonProperty("is_verified_email")
    private boolean isVerifiedEmail;

    private String image;
    private String header;
    private boolean delivery;

    @JsonProperty("delivery_price")
    private Double deliveryPrice;

    @JsonProperty("delivery_radius")
    private Integer deliveryRadius;

    private AddressResponse address;

    @JsonProperty("business_hours")
    private List<BusinessHoursResponse> businessHours;

    private CompanyStatus status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
