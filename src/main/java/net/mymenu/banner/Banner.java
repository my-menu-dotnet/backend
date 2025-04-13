package net.mymenu.banner;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.category.Category;
import net.mymenu.banner.enums.BannerRedirect;
import net.mymenu.banner.enums.BannerType;
import net.mymenu.food.Food;
import net.mymenu.file_storage.FileStorage;
import net.mymenu.tenant.BaseEntity;

@Entity
@Table(name = "banner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banner extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileStorage image;

    @Column(name = "redirect")
    @Enumerated(EnumType.STRING)
    private BannerRedirect redirect;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Food food;

    @Column(name = "url")
    private String url;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BannerType type;
}
