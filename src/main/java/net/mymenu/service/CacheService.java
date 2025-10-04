package net.mymenu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    @CacheEvict(value = {"analytics", "monthlyTicket", "dailyStats", "itemStats"}, allEntries = true)
    public void evictAllAnalyticsCache() {
        // Este método limpa todo o cache de analytics quando chamado
    }

    @CacheEvict(value = "analytics", allEntries = true)
    public void evictAnalyticsCache() {
        // Limpa apenas o cache do analytics completo
    }

    @CacheEvict(value = "monthlyTicket", allEntries = true)
    public void evictMonthlyTicketCache() {
        // Limpa apenas o cache do ticket médio mensal
    }

    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            cacheManager.getCache(cacheName).clear();
        });
    }
}
