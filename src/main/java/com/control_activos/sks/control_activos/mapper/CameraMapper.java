package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditResponseDTO;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Camera;

import java.time.OffsetDateTime;

public class CameraMapper {

    public static Camera toCameraEntity(CameraEditRequestDTO cameraEditRequestDTO, Branch branch) {
        Camera camera = new Camera();
        camera.setName(cameraEditRequestDTO.getName());
        camera.setBrand(cameraEditRequestDTO.getBrand());
        camera.setSerialNumber(cameraEditRequestDTO.getSerialNumber());
        camera.setModel(cameraEditRequestDTO.getModel());
        camera.setLocation(cameraEditRequestDTO.getLocation());
        camera.setLastUpdate(OffsetDateTime.now());
        camera.setBranch(branch);
        camera.setCameraId(cameraEditRequestDTO.getCameraId());
        camera.setMacAddress(cameraEditRequestDTO.getMacAddress());
        camera.setIpAddress(cameraEditRequestDTO.getIpAddress());
        camera.setIdf(cameraEditRequestDTO.getIdf());
        camera.setUsername(cameraEditRequestDTO.getUsername());
        camera.setPassword(cameraEditRequestDTO.getPassword());
        return camera;
    }

    public static CameraEditResponseDTO toCameraEditResponseDto(Camera camera) {
        return new CameraEditResponseDTO(
                camera.getId(),
                camera.getName(),
                camera.getBrand(),
                camera.getSerialNumber(),
                camera.getModel(),
                camera.getLocation(),
                camera.getCameraId(),
                camera.getMacAddress(),
                camera.getIpAddress(),
                camera.getIdf(),
                camera.getUsername(),
                camera.getPassword()
        );
    }
}
