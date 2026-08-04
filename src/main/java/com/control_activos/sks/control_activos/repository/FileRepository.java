package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<FileEntity, Long> {
}
