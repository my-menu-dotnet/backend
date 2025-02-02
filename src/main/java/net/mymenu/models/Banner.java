package net.mymenu.models;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.banner.BannerRedirect;
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileStorage image;

    @Column(name = "redirect")
    @Enumerated(EnumType.STRING)
    private BannerRedirect redirect;

    @Column(name = "url")
    private String url;

    @Column(name = "\"order\"")
    private Integer order;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private Company company;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
