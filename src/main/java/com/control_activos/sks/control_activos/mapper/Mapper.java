package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.enums.ReportPriorityEnum;
import com.control_activos.sks.control_activos.models.dto.*;
import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportDTO;
import com.control_activos.sks.control_activos.models.dto.user.UserEntityResponseDTO;
import com.control_activos.sks.control_activos.models.entity.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Mapper {

    // #TODO: Implments Mapper Class for each entity to DTO conversion, and vice versa if needed.
    /*
    public static  entityToDTO (Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                Optional.ofNullable(client.getBranches()).orElse(List.of())
                        .stream().map(Mapper::entityToDTO).toList()
        );
    }*/

    public static CommentDTO entityToDTO (Comment comment) {
        return new  CommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getUser().getFullName(),
                comment.getCreatedAt()
        );
    }

    public static FileEntityDTO entityToDTO (FileEntity fileEntity) {
        FileEntityDTO dto = new FileEntityDTO();
        dto.setId(fileEntity.getId());
        dto.setFilename(fileEntity.getFilename());
        dto.setContentType(fileEntity.getContentType());
        return dto;
    }

    public static ReportDTO entityToDTO (Report report) {
        return new ReportDTO(
                report.getId(),
                report.getTitle(),
                Optional.ofNullable(report.getFileEntities()).orElse(List.of()).stream().map(Mapper::entityToDTO).toList(),
                Optional.ofNullable(report.getComments()).orElse(List.of()).stream().map(Mapper::entityToDTO).toList(),
                report.getStatus(),
                report.getHardware().getName(),
                report.getReportedBy().getFullName(),
                report.getCreatedAt().toString(),
                Optional.ofNullable(report.getUpdatedAt()).map(OffsetDateTime::toString).orElse("N/A"),
                Optional.ofNullable(report.getClosedAt()).map(OffsetDateTime::toString).orElse("N/A"),
                Optional.ofNullable(report.getDueDate()).map(OffsetDateTime::toString).orElse("N/A"),
                Optional.ofNullable(report.getPriority()).map(ReportPriorityEnum::getValue).orElse("N/A")
        );
    }

    public static BranchDTO entityToDTO (Branch branch) {
        return new BranchDTO(
                branch.getId(),
                branch.getName()
        );
    }

    public static UserEntityResponseDTO entityToDTO (UserEntity user) {
        return new UserEntityResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().getValue(),
                user.isActive()
        );
    }
}
