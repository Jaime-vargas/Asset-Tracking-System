package com.control_activos.sks.control_activos.dtos;

import com.control_activos.sks.control_activos.models.Comment;
import com.control_activos.sks.control_activos.models.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
}
