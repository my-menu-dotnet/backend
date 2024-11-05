package com.digimenu.repository;

import com.digimenu.models.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorage, UUID> {
}
