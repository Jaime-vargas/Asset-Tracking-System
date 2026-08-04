package com.control_activos.sks.control_activos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String filename;
    @NotNull
    private String contentType;
    @NotNull
    private Long size;
    @NotNull
    private String filePath;
    @NotNull
    private OffsetDateTime uploadedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private FileCategory category;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public FileEntity(String filename, String contentType, Long size, String filePath, OffsetDateTime uploadedAt) {
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }
}
