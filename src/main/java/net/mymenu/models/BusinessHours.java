package net.mymenu.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "business_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHours {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    @JsonProperty("day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "opening_time")
    @JsonProperty("opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    @JsonProperty("closing_time")
    private LocalTime closingTime;

    @Column(name = "is_closed")
    @JsonProperty("is_closed")
    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
