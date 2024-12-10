package net.mymenu.dto.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryOrder {
    private List<UUID> ids;
}
