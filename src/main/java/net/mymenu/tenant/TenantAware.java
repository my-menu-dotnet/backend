package net.mymenu.tenant;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TenantAware {
    UUID getTenantId();
}
