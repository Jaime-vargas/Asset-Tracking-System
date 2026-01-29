package com.control_activos.sks.control_activos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PhotoDTO {
    private Long id;
    private String filename;
    private String contentType;
    private String filePath;
}
