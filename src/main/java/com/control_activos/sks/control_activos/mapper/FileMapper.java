package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.entity.FileEntity;

public class FileMapper {
    public static FileEntityDTO toFileEntityDto(FileEntity fileEntity) {
        return new FileEntityDTO(
                fileEntity.getId(),
                fileEntity.getFilename(),
                fileEntity.getContentType(),
                fileEntity.getSize(),
                fileEntity.getFilePath(),
                fileEntity.getUploadedAt(),
                fileEntity.getCategory().getName(),
                fileEntity.getUser().getFullName()
        );
    }
}
