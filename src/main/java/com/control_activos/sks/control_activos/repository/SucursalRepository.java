package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal,Long> {
}
