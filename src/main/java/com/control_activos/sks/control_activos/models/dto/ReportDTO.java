package com.control_activos.sks.control_activos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private String title;
    private List<PhotoDTO> photos;
    private List<CommentDTO> comments;
    private Boolean active;
    private String hardware;
    private String reportedBy;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
}
