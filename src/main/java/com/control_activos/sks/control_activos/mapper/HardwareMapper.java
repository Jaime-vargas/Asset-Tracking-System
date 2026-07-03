package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.hardwareDTO.CameraDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.models.entity.Hardware;

import java.util.Optional;

public class HardwareMapper {

    public static HardwareDetailDTO hardwareDetailDTO(Hardware hardware) {
        if(hardware instanceof Camera camera){
            return new CameraDetailDTO(
                    camera.getId(),
                    camera.getClass().getSimpleName(),
                    camera.getBrand(),
                    camera.getModel(),
                    camera.getSerialNumber(),
                    camera.getLocation(),
                    camera.getName(),
                    Optional.ofNullable(camera.getLastUpdate()).map(Object::toString).orElse("N/A"),


                    //camera.getReports().size(),



                    camera.getCameraId(),
                    camera.getMacAddress(),
                    camera.getIpAddress(),
                    camera.getIdf(),
                    camera.getUsername(),
                    camera.getPassword(),
                    camera.getViewFromCameraPhoto() != null ? PhotoMapper.toPhotoDTO(camera.getViewFromCameraPhoto()) : null,
                    camera.getViewToCameraPhoto() != null ? PhotoMapper.toPhotoDTO(camera.getViewToCameraPhoto()) : null
            );
        }
        else return null; // TODO: Add other hardware types and throw exception if type is not supported
    }

    public static HardwareTableDTO toHardwareTableDTO(Hardware hardware) {
        return new HardwareTableDTO(
            hardware.getId(),
            hardware.getClass().getSimpleName(),


            hardware.getName(),
            hardware.getBrand(),
            hardware.getModel(),
            hardware.getSerialNumber(),
            hardware.getLocation(),


            Optional.ofNullable(hardware.getLastUpdate()).map(Object::toString).orElse("N/A"),
            hardware.getBranch().getClient().getId(),
            hardware.getBranch().getClient().getName(),
            hardware.getBranch().getId(),
            hardware.getBranch().getName()
        );
    }
}
