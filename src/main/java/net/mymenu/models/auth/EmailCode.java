package net.mymenu.models.auth;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.enums.auth.EmailCodeType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_code", indexes = {
        @Index(name = "email_code_type_index", columnList = "type"),
        @Index(name = "email_code_code_index", columnList = "code")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EmailCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EmailCodeType type;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
