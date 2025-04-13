package net.mymenu.auth.refresh_token;

import jakarta.persistence.*;
import lombok.*;
import net.mymenu.timestamp.Timestamped;
import net.mymenu.timestamp.TimestampedListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@EntityListeners({TimestampedListener.class})
public class RefreshToken implements Timestamped {
    @Id
    private String token;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}