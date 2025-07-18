package net.mymenu.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    @JsonProperty("preference_id")
    String preferenceId;
}
