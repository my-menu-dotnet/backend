package com.digimenu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {

    @JsonProperty("user_id")
    private UUID userId;

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
