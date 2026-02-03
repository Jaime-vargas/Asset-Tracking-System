package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.Switch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwitchRepository extends JpaRepository<Switch, Long> {
}
