package com.digimenu.models;

import com.digimenu.constraints.CEP;
import com.digimenu.constraints.State;
import com.digimenu.interfaces.Timestamped;
import com.digimenu.listeners.CoordinatesEntityListener;
import com.digimenu.listeners.TimestampedListener;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners({TimestampedListener.class, CoordinatesEntityListener.class})
public class Address implements Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    @State
    private String state;

    @Column(name = "zip_code")
    @JsonProperty("zip_code")
    @CEP
    private String zipCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
