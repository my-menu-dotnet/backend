package net.mymenu.dto.banner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mymenu.enums.banner.BannerRedirect;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequest {

    @JsonProperty("image_id")
    private UUID imageId;

    private BannerRedirect redirect;

    private String url;

    private Boolean active;
}
