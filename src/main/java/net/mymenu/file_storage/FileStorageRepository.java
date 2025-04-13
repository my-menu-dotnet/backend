package net.mymenu.file_storage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorage, UUID> {
}
