package net.mymenu.company.allowed_field;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "allowed_field")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllowedField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "field")
    private String field;
}
