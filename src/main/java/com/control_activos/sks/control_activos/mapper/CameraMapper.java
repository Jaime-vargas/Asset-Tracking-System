package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.enums.CameraImportColumns;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditResponseDTO;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Camera;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

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

    public static CameraEditRequestDTO toCameraEditRequestDTO(Row row, DataFormatter formatter ) {
        return new CameraEditRequestDTO(
                formatter.formatCellValue(row.getCell(CameraImportColumns.NAME.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.BRAND.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.SERIAL_NUMBER.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.MODEL.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.LOCATION.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.CAMERA_ID.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.MAC_ADDRESS.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.IP_ADDRESS.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.IDF.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.USERNAME.getIndex())),
                formatter.formatCellValue(row.getCell(CameraImportColumns.PASSWORD.getIndex()))
        );
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
