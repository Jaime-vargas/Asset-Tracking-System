package com.control_activos.sks.control_activos.dtos;


import com.control_activos.sks.control_activos.models.Report;
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
public class CameraDTO {

    private Long id;
    private String name;
    private String serialNumber;
    private String model;
    private String location;
    private LocalDateTime lastMaintenanceDate;
    private String sucursal;
    private String cliente;
    private String idCamera;
    private String macAddress;
    private String ipAddress;
    private List<ReportDTO> reports;

}
