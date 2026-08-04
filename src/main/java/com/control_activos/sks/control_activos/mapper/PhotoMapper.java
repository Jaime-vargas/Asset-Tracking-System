package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.enums.UserRoleEnum;
import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.entity.FileCategory;
import com.control_activos.sks.control_activos.models.entity.FileEntity;
import com.control_activos.sks.control_activos.models.entity.UserEntity;

import java.util.Optional;

public class PhotoMapper {

    public static FileEntityDTO toPhotoDTO(FileEntity photo) {
        UserEntity userEntity = Optional.ofNullable(photo.getUser()).orElse(new UserEntity());
        return new FileEntityDTO(
                photo.getId(),
                photo.getFilename(),
                photo.getContentType(),
                photo.getSize(),
                photo.getFilePath(),
                photo.getUploadedAt(),
                Optional.ofNullable(photo.getCategory())
                        .map(FileCategory::getName)
                        .orElse(""),
                userEntity.getFullName()
        );
    }
}
