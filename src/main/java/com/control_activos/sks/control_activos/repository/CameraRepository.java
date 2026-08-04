package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    List<Camera> findByBranchId(Long branchId);
    boolean existsByIdAndBranchId(Long id, Long branch_id);

    boolean existsByCameraIdAndBranchIdAndIdNot(String cameraId, Long branchId, Long id);
    boolean existsByNameAndBranchIdAndIdNot(String name, Long branchId, Long id);
    boolean existsBySerialNumberAndBranchIdAndIdNot(String serialNumber, Long branchId, Long id);
    boolean existsByMacAddressAndBranchIdAndIdNot(String macAddress, Long branchId, Long id);
    boolean existsByIpAddressAndBranchIdAndIdNot(String ipAddress, Long branchId, Long id);
}
