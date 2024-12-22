package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.mymenu.enums.CompanyStatus;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "company", indexes = {
        @Index(name = "idx_company_name", columnList = "name"),
        @Index(name = "idx_company_cnpj", columnList = "cnpj"),
        @Index(name = "idx_company_email", columnList = "email"),
        @Index(name = "idx_company_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(TimestampedListener.class)
@Builder
public class Company implements Timestamped {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "url")
    private String url;

    @Column(name = "primary_color")
    private String primaryColor;

    @Column(name = "is_verified_email")
    @ColumnDefault("false")
    private boolean isVerifiedEmail;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileStorage image;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private FileStorage header;

    @Column(name = "delivery", nullable = false)
    @ColumnDefault("false")
    private boolean delivery;

    @Column(name = "delivery_price")
    @JsonProperty("delivery_price")
    private Double deliveryPrice;

    @Column(name = "delivery_radius")
    @JsonProperty("delivery_radius")
    private Integer deliveryRadius;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @OrderBy("order ASC")
    private List<Category> categories;

    @ManyToMany
    private List<AllowedField> allowedField;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private CompanyStatus status;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public boolean isVerifiedEmail() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        this.status = CompanyStatus.ACTIVE;
        this.delivery = false;
        this.isVerifiedEmail = false;
    }
}
