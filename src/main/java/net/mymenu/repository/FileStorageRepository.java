package net.mymenu.repository;

import net.mymenu.models.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorage, UUID> {
}
