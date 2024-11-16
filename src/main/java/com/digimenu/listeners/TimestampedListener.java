package com.digimenu.listeners;

import com.digimenu.interfaces.Timestamped;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampedListener {

    @PreUpdate
    public void preUpdate(Timestamped entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
