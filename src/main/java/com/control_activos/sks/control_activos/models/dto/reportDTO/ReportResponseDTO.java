package com.control_activos.sks.control_activos.models.dto.reportDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportResponseDTO {
    private Long id;
    private String title;
    private String priorityEnum;
    private String reportDetails;
    private String status;
    private String dueDate;
}
