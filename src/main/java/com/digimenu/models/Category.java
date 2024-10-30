package com.digimenu.models;

import com.digimenu.enums.CategoryStatus;
import com.digimenu.listeners.CompanyEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@EntityListeners(CompanyEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements CompanyAware {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "status")
    private CategoryStatus status;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "category")
    private List<Food> food;
}
