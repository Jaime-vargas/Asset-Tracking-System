package com.control_activos.sks.control_activos.models.dto.reportDTO;

import com.control_activos.sks.control_activos.enums.ReportPriorityEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Getter
public class ReportRequestDTO {
    private String title;
    private String priorityEnum;
    private String reportDetails;
    private String dueDate;
}
