package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.CategoryMapper;
import com.control_activos.sks.control_activos.models.dto.FileCategoryDTO;
import com.control_activos.sks.control_activos.models.entity.FileCategory;
import com.control_activos.sks.control_activos.repository.FileCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileCategoryService {

    private final FileCategoryRepository fileCategoryRepository;

    public List<FileCategoryDTO> getCategories(){
        return this.fileCategoryRepository.findAllByOrderByNameAsc().stream().map(CategoryMapper::toCategoryDTO).collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public FileCategoryDTO saveFileCategory(FileCategoryDTO fileCategoryDTO){
        FileCategory category = new FileCategory();
        category.setName(fileCategoryDTO.getName());
        category = fileCategoryRepository.save(category);
        return CategoryMapper.toCategoryDTO(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public FileCategoryDTO updateFileCategory(Long id, FileCategoryDTO fileCategoryDTO) {
        FileCategory category = findById(id);
        category.setName(fileCategoryDTO.getName());
        return CategoryMapper.toCategoryDTO(category);
    }



    public FileCategory findById(Long id){
        return fileCategoryRepository.findById(id)
                .orElseThrow( () ->
                    new ResourceNotFoundException(
                            ResourceNotFoundExceptionEnum.CATEGORY_NOT_FOUND.build(id)
                    )
                );
    }

}
