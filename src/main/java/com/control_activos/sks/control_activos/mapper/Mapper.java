package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.dtos.*;
import com.control_activos.sks.control_activos.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Mapper {

    public static CameraDTO entityToDTO (Camera camera) {
        CameraDTO dto = new CameraDTO();
        dto.setId(camera.getId());
        dto.setName(camera.getName());
        dto.setSerialNumber(camera.getSerialNumber());
        dto.setModel(camera.getModel());
        dto.setLocation(camera.getLocation());
        dto.setLastMaintenanceDate(camera.getLastMaintenanceDate());
        dto.setSucursal(camera.getSucursal().getName());
        dto.setCliente(camera.getSucursal().getClient().getName());
        dto.setIdCamera(camera.getIdCamera());
        dto.setMacAddress(camera.getMacAddress());
        dto.setIpAddress(camera.getIpAddress());
        // Convert List<Report> to List<ReportDTO> //
        List<ReportDTO> reportDTOList = camera.getReports().stream().map(Mapper::entityToDTO).toList();
        dto.setReports(reportDTOList);
        return dto;
    }

    public static ClientDTO entityToDTO (Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        // Convert List<Sucursal> to List<String> //
        List<String> sucursalNames = client.getSucursals().stream().map(Sucursal::getName).toList();
        dto.setSucursalList(sucursalNames);
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
