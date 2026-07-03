package com.control_activos.sks.control_activos.models.dto;

import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String dueDate;
    private String priority;
}
