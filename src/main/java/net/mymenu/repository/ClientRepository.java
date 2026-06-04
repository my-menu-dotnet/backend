package net.mymenu.repository;

import net.mymenu.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("""
            SELECT c FROM Client c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ORDER BY c.name ASC
            """)
    List<Client> searchByName(@Param("name") String name, Pageable pageable);

    Page<Client> findAll(Pageable pageable);

    long removeById(UUID id);
}
