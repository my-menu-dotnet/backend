package net.mymenu.config;

import net.mymenu.tenant.BaseEntity;
import net.mymenu.tenant.TenantContext;
import net.mymenu.tenant.TenantIdentifierResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@Import({TenantIdentifierResolver.class})
public abstract class TenantTest<T extends BaseEntity> {

    protected static final UUID TENANT_ID = UUID.randomUUID();
    protected static final UUID OTHER_TENANT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TENANT_ID);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Should save with tenant")
    void shouldSaveWithTenant() {
        var repository = getRepository();
        var entity = getEntity();

        BaseEntity savedEntity = repository.save(entity);
        assertEquals(TENANT_ID, savedEntity.getTenantId());
    }

    @Test
    @DisplayName("Should find from current tenant")
    void shouldOnlyFindFromCurrentTenant() {
        var repository = getRepository();
        var entity = getEntity();

        repository.save(entity);

        TenantContext.setCurrentTenant(OTHER_TENANT_ID);
        var otherEntity = getEntity();
        repository.save(otherEntity);

        TenantContext.setCurrentTenant(TENANT_ID);
        List<T> categories = repository.findAll();
        assertTrue(categories.stream().allMatch(category -> category.getTenantId().equals(TENANT_ID)));
    }

    @Test
    @DisplayName("Should update within same tenant")
    void shouldUpdateWithinSameTenant() {
        var repository = getRepository();
        var entity = getEntity();

        LocalDateTime now = LocalDateTime.now();

        T saved = repository.save(entity);
        T updated = repository.save(saved);

        boolean isAfter = updated.getUpdatedAt().isAfter(now) || updated.getUpdatedAt().isEqual(now);

        assertTrue(isAfter);
        assertEquals(TENANT_ID, updated.getTenantId());
    }

    @Test
    @DisplayName("Should delete from current tenant")
    void shouldDeleteFromCurrentTenant() {
        var repository = getRepository();
        var entity = getEntity();

        T saved = repository.save(entity);

        repository.deleteById(saved.getId());

        Optional<T> deleted = repository.findById(saved.getId());
        Optional<T> search = repository.findById(saved.getId());

        assertTrue(deleted.isEmpty());
        assertTrue(search.isEmpty());
    }


    @Test
    @DisplayName("Should not find category from other tenant")
    void shouldNotFindFromOtherTenant() {
        var repository = getRepository();
        var entity = getEntity();

        T saved = repository.save(entity);

        TenantContext.setCurrentTenant(OTHER_TENANT_ID);

        Exception exception = assertThrows(SecurityException.class, () ->
                repository.deleteById(saved.getId()));
        assertEquals("Entity's tenantId does not match current tenant", exception.getMessage());
    }

    @Test
    @DisplayName("Should not update category from other tenant")
    void shouldNotUpdateFromOtherTenant() {
        var repository = getRepository();
        var entity = getEntity();

        T saved = repository.save(entity);

        TenantContext.setCurrentTenant(OTHER_TENANT_ID);

        Exception exception = assertThrows(SecurityException.class, () ->
                repository.save(saved));
        assertEquals("Entity's tenantId does not match current tenant", exception.getMessage());
    }

    @Test
    @DisplayName("Should not delete category from other tenant")
    void shouldNotDeleteFromOtherTenant() {
        var repository = getRepository();
        var entity = getEntity();

        T saved = repository.save(entity);

        TenantContext.setCurrentTenant(OTHER_TENANT_ID);

        Exception exception = assertThrows(SecurityException.class, () ->
                repository.deleteById(saved.getId()));
        assertEquals("Entity's tenantId does not match current tenant", exception.getMessage());
    }

    protected abstract T getEntity();
    protected abstract JpaRepository<T, UUID> getRepository();
}

