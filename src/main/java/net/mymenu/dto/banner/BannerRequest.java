package net.mymenu.dto.banner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mymenu.enums.banner.BannerRedirect;
import net.mymenu.enums.banner.BannerType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequest {

    @NotNull
    private String title;

    private String description;

    @JsonProperty("image_id")
    private UUID imageId;

    @JsonProperty("category_id")
    private UUID categoryId;

    @JsonProperty("food_id")
    private UUID foodId;

    private BannerRedirect redirect;

    @NotNull
    private BannerType type;

    private String url;

    private Boolean active;
}
