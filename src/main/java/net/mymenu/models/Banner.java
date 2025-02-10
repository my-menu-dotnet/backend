package net.mymenu.models;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.banner.BannerRedirect;
import net.mymenu.enums.banner.BannerType;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "banner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({TimestampedListener.class})
public class Banner implements Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @ManyToOne
    private Company company;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
