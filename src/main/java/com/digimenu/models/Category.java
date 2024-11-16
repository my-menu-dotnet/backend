package com.digimenu.models;

import com.digimenu.enums.CategoryStatus;
import com.digimenu.interfaces.CompanyAware;
import com.digimenu.interfaces.Timestamped;
import com.digimenu.listeners.CompanyEntityListener;
import com.digimenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({CompanyEntityListener.class, TimestampedListener.class})
public class Category implements CompanyAware, Timestamped {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private FileStorage image;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Food> food;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Category(String name, String description, FileStorage image, CategoryStatus status, Company company) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.status = status;
        this.company = company;
    }
}
