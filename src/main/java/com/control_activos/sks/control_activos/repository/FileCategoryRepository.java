package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileCategoryRepository extends JpaRepository<FileCategory, Long> {
    List<FileCategory> findAllByOrderByNameAsc();
}
