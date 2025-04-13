package net.mymenu.address;

import net.mymenu.constraints.CEP;
import net.mymenu.constraints.State;
import net.mymenu.timestamp.Timestamped;
import net.mymenu.timestamp.TimestampedListener;
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
@EntityListeners({TimestampedListener.class})
public class Address implements Timestamped {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @Column(name = "validated")
    private Boolean validated;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void removeCepMask() {
        this.zipCode = this.zipCode.replaceAll("[^0-9]", "");
    }

}
