package net.mymenu.banner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BannerRepository extends JpaRepository<Banner, UUID> {
    void removeById(UUID id);

    List<Banner> findAllByActiveTrue();
}
