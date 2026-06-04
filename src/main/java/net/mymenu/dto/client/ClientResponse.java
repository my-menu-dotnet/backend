package net.mymenu.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    private UUID id;

    private String name;

    private String email;

    private String phone;

    private String cpf;

    private AddressPayload address;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressPayload {
        private UUID id;
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String city;
        private String state;

        @JsonProperty("zip_code")
        private String zipCode;
    }
}
