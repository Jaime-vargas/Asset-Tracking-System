package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.*;
import com.control_activos.sks.control_activos.models.entity.*;
import org.springframework.stereotype.Service;
import tools.jackson.databind.introspect.TypeResolutionContext;

import java.util.List;
import java.util.Optional;

import static tools.jackson.databind.cfg.CoercionInputShape.EmptyString;

@Service
public class Mapper {

    public static CameraDTO entityToDTO (Camera camera) {
        String lastMaintenanceDate = camera.getLastMaintenanceDate() != null ?
                camera.getLastMaintenanceDate().toString() : "N/A";

        List<ReportDTO> reportDTOList = Optional.ofNullable(camera.getReports())
                .orElse(List.of())
                .stream()
                .map(Mapper::entityToDTO)
                .toList();

        return new CameraDTO(
                camera.getId(),
                camera.getCameraId(),
                camera.getName(),
                camera.getSerialNumber(),
                camera.getModel(),
                camera.getLocation(),
                lastMaintenanceDate,
                camera.getSucursal().getName(),
                camera.getSucursal().getClient().getName(),
                camera.getMacAddress(),
                camera.getIpAddress(),
                reportDTOList
        );
    }

    public static ClientDTO entityToDTO (Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        List<SucursalDTO> sucursalDTOList = client.getSucursals().stream().map(Mapper::entityToDTO).toList();
        dto.setSucursalDTOList(sucursalDTOList);
        return dto;
    }

    public static CommentDTO entityToDTO (Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        return dto;
    }

    public static PhotoDTO entityToDTO (Photo photo) {
        PhotoDTO dto = new PhotoDTO();
        dto.setId(photo.getId());
        dto.setFilename(photo.getFilename());
        dto.setContentType(photo.getContentType());
        dto.setFilePath(photo.getFilePath());
        return dto;
    }

    public static ReportDTO entityToDTO (Report report) {
        // DATES
        String createdAt = report.getCreatedAt().toString();
        String updatedAt = report.getUpdatedAt() != null ?
                report.getUpdatedAt().toString() : "N/A";
        String closedAt = report.getClosedAt() != null ?
                report.getClosedAt().toString() : "N/A";

        List<PhotoDTO> photoDTOList = Optional.ofNullable(report.getPhotos())
                .orElse(List.of())
                .stream()
                .map(Mapper::entityToDTO)
                .toList();
        List<CommentDTO> commentDTOList = Optional.ofNullable(report.getComments())
                .orElse(List.of())
                .stream()
                .map(Mapper::entityToDTO)
                .toList();

        return new ReportDTO(
                report.getId(),
                report.getTitle(),
                photoDTOList,
                commentDTOList,
                report.getActive(),
                report.getHardware().getName(),
                report.getReportedBy().getFullName(),
                createdAt,
                updatedAt,
                closedAt
        );
    }

    public static SucursalDTO entityToDTO (Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(sucursal.getId());
        dto.setName(sucursal.getName());
        return dto;
    }

    public static UserEntityResponseDTO entityToDTO (UserEntity user) {
        return new UserEntityResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().getValue()
        );
    }
}
