package net.mymenu.tenant;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantAwareEntityListener {

    @PrePersist
    @PreUpdate
    @PreRemove
    @PostLoad
    public void checkTenant(Object entity) {
        if (!(entity instanceof TenantAware)) return;
        UUID currentTenantIdentifier = TenantContext.getCurrentTenant();

        if (currentTenantIdentifier == null) {
            return;
        }

        if (!currentTenantIdentifier.equals(((TenantAware) entity).getTenantId())) {
            log.warn("Entity's tenantId does not match current tenant: currentTenantIdentifier={}, entity={}", currentTenantIdentifier, entity);
            throw new SecurityException("Entity's tenantId does not match current tenant");
        }
    }
}
