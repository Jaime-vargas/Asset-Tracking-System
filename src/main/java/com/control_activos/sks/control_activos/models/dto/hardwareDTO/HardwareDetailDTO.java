package com.control_activos.sks.control_activos.models.dto.hardwareDTO;

import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public abstract class HardwareDetailDTO {
    private Long id;
    private String type;
    private String name;
    private String brand;
    private String model;
    private String serialNumber;
    private String location;
    private String lastMaintenanceDate;
    private long activeReportsCount;
    private long overdueReportsCount;
    private List<ReportHistoryDTO> recentActiveReports;

    public HardwareDetailDTO(long id, String type, String brand, String model, String serialNumber, String location, String name, String lastMaintenanceDate) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.location = location;
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
}
