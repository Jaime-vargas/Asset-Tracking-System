package com.control_activos.sks.control_activos.models.dto.clientDTO;

import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportCountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class ClientTableDTO {
    Long id;
    String name;
    Long branches;
    Long totalHardware;
    List<ReportCountDTO> reportsActive;
    FileEntityDTO photo;
}
