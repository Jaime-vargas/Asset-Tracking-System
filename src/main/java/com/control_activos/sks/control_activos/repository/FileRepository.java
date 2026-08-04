package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
