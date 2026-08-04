package com.control_activos.sks.control_activos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PhotoDTO {
    private Long id;
    private String filename;
    private String contentType;
    private String filePath;
    private OffsetDateTime uploadedAt;
}
