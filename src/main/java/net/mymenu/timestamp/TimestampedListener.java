package net.mymenu.timestamp;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampedListener {

    @PreUpdate
    public void preUpdate(Timestamped entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @PrePersist
    public void prePersist(Timestamped entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
