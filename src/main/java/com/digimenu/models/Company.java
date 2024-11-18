package com.digimenu.models;

import com.digimenu.enums.CompanyStatus;
import com.digimenu.interfaces.Timestamped;
import com.digimenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "company")
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

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @ManyToMany
    @JoinTable(
            name = "company_category",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToOne
    private FileStorage image;

    @Column(name = "delivery")
    private Boolean delivery;

    @Column(name = "delivery_price")
    @JsonProperty("delivery_price")
    private Double deliveryPrice;

    @Column(name = "delivery_radius")
    @JsonProperty("delivery_radius")
    private Integer deliveryRadius;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void addStatus() {
        this.status = CompanyStatus.ACTIVE;
    }
}
