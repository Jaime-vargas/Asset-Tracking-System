package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    List<Camera> findBySucursalId(Long sucursalId);

    boolean existsByCameraIdAndSucursalIdAndIdNot(String cameraId, Long sucursalId, Long id);
    boolean existsByNameAndSucursalIdAndIdNot(String name, Long sucursalId, Long id);
    boolean existsBySerialNumberAndSucursalIdAndIdNot(String serialNumber, Long sucursalId, Long id);
    boolean existsByMacAddressAndSucursalIdAndIdNot(String macAddress, Long sucursalId, Long id);
    boolean existsByIpAddressAndSucursalIdAndIdNot(String ipAddress, Long sucursalId, Long id);
}
