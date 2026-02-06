package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.*;
import com.control_activos.sks.control_activos.models.entity.*;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Mapper {

    public static CameraDTO entityToDTO (Camera camera) {
        return new CameraDTO(
                camera.getId(),
                camera.getCameraId(),
                camera.getName(),
                camera.getSerialNumber(),
                camera.getModel(),
                camera.getLocation(),
                Optional.ofNullable(camera.getLastMaintenanceDate()).map(OffsetDateTime::toString).orElse("N/A"),
                camera.getSucursal().getName(),
                camera.getSucursal().getClient().getName(),
                camera.getMacAddress(),
                camera.getIpAddress(),
                Optional.ofNullable(camera.getReports()).orElse(List.of()).stream()
                        .map(Mapper::entityToDTO).toList()
        );
    }

    public static ClientDTO entityToDTO (Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                Optional.ofNullable(client.getSucursals()).orElse(List.of())
                        .stream().map(Mapper::entityToDTO).toList()
        );
    }

    public static CommentDTO entityToDTO (Comment comment) {
        return new  CommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getUser().getFullName()
        );
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
        return new ReportDTO(
                report.getId(),
                report.getTitle(),
                Optional.ofNullable(report.getPhotos()).orElse(List.of()).stream().map(Mapper::entityToDTO).toList(),
                Optional.ofNullable(report.getComments()).orElse(List.of()).stream().map(Mapper::entityToDTO).toList(),
                report.getActive(),
                report.getHardware().getName(),
                report.getReportedBy().getFullName(),
                report.getCreatedAt().toString(),
                Optional.ofNullable(report.getUpdatedAt()).map(OffsetDateTime::toString).orElse("N/A"),
                Optional.ofNullable(report.getClosedAt()).map(OffsetDateTime::toString).orElse("N/A")
        );
    }

    public static SucursalDTO entityToDTO (Sucursal sucursal) {
        return new SucursalDTO(
                sucursal.getId(),
                sucursal.getName()
        );
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
