package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.FileCategoryDTO;
import com.control_activos.sks.control_activos.repository.FileCategoryRepository;
import com.control_activos.sks.control_activos.services.FileCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file-categories")
@RequiredArgsConstructor
public class FileCategoryController {

    private final FileCategoryService fileCategoryService;

    @GetMapping
    public ResponseEntity<List<FileCategoryDTO>> getFileCategoriesByBranchId() {
        List<FileCategoryDTO> categories = this.fileCategoryService.getCategories();
        return ResponseEntity.ok().body(categories);
    }

    @PostMapping
    public ResponseEntity<FileCategoryDTO> saveFileCategory(@RequestBody FileCategoryDTO fileCategoryDTO){
        FileCategoryDTO savedCategory = fileCategoryService.saveFileCategory(fileCategoryDTO);
        return ResponseEntity.ok().body(savedCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<FileCategoryDTO> updateFileCategory(@PathVariable Long categoryId, @RequestBody FileCategoryDTO fileCategoryDTO){
        FileCategoryDTO updatedCategory = fileCategoryService.updateFileCategory(categoryId, fileCategoryDTO);
        return ResponseEntity.ok().body(updatedCategory);
    }
}
