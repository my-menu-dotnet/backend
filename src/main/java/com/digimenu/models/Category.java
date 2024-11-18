package com.digimenu.models;

import com.digimenu.enums.CategoryStatus;
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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({TimestampedListener.class})
@Builder
public class Category implements Timestamped {

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

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
