package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.*;
import com.control_activos.sks.control_activos.models.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setTitle(report.getTitle());
        // Convert List<Photo> to List<PhotoDTO> //
        List<PhotoDTO> photoDTOList = report.getPhotos().stream().map(Mapper::entityToDTO).toList();
        dto.setPhotos(photoDTOList);
        // Convert List<Comment> to List<CommentDTO> //
        List<CommentDTO> commentDTOList = report.getComments().stream().map(Mapper::entityToDTO).toList();
        dto.setComments(commentDTOList);
        dto.setActive(report.getActive());
        dto.setHardware(report.getHardware().getName());
        dto.setReportedBy(report.getReportedBy().getUsername());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        dto.setClosedAt(report.getClosedAt());
        return dto;
    }

    public static SucursalDTO entityToDTO (Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(sucursal.getId());
        dto.setName(sucursal.getName());
        return dto;
    }
}
