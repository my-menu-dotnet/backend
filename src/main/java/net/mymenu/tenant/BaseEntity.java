package net.mymenu.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.mymenu.interfaces.TenantAware;
import net.mymenu.interfaces.Timestamped;
import net.mymenu.listeners.TenantAwareEntityListener;
import net.mymenu.listeners.TimestampedListener;
import org.hibernate.annotations.TenantId;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@EntityListeners({TenantAwareEntityListener.class, TimestampedListener.class})
public class BaseEntity implements TenantAware, Timestamped {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    @JsonIgnore
    @TenantId
    private UUID tenantId = TenantContext.getCurrentTenant();

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
