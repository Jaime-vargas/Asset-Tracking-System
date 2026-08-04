package com.control_activos.sks.control_activos.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FileEntityDTO {
    private Long id;
    private String filename;
    private String contentType;
    private Long size;
    private String filePath;
    private OffsetDateTime uploadedAt;
    private String category;
    private String user;
}
