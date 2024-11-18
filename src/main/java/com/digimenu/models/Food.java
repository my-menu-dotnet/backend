package com.digimenu.models;

import com.digimenu.enums.FoodStatus;
import com.digimenu.interfaces.CompanyAware;
import com.digimenu.interfaces.Timestamped;
import com.digimenu.listeners.CompanyEntityListener;
import com.digimenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({TimestampedListener.class, CompanyEntityListener.class})
@Builder
public class Food implements Timestamped, CompanyAware {

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private FileStorage image;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @Column(name = "lactose_free")
    private boolean lactoseFree;

    @Column(name = "gluten_free")
    private boolean glutenFree;

    @Column(name = "vegan")
    private boolean vegan;

    @Column(name = "vegetarian")
    private boolean vegetarian;

    @Column(name = "halal")
    private boolean halal;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
