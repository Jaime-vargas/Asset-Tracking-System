package com.control_activos.sks.control_activos.mapper;

import com.control_activos.sks.control_activos.models.dto.FileCategoryDTO;
import com.control_activos.sks.control_activos.models.entity.FileCategory;


public class CategoryMapper {
    public static FileCategoryDTO toCategoryDTO(FileCategory category){
        return new FileCategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
