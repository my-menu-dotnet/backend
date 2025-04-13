package net.mymenu.timestamp;

import java.time.LocalDateTime;

public interface Timestamped {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}
