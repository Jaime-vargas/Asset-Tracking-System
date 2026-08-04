package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.FileCategory;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<FileCategory, Long> {

}
