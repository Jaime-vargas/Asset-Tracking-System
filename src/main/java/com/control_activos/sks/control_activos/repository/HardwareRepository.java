package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
}
