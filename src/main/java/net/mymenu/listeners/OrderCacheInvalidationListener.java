package net.mymenu.listeners;

import net.mymenu.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

@Component
public class OrderCacheInvalidationListener extends AuditingEntityListener {

    @Autowired
    private CacheService cacheService;

    @PostPersist
    @PostUpdate
    public void onOrderChange(Object entity) {
        // Limpa o cache de analytics quando um pedido é criado ou atualizado
        if (cacheService != null) {
            cacheService.evictAllAnalyticsCache();
        }
    }
}
