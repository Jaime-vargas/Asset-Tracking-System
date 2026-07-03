package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportHistoryDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportRequestDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportTableDTO;
import com.control_activos.sks.control_activos.services.HardwareService;
import com.control_activos.sks.control_activos.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hardware")
@AllArgsConstructor
public class HardwareController {

    private final HardwareService hardwareService;
    private final ReportService reportService;

    @GetMapping()
    public ResponseEntity<List<HardwareTableDTO>> getAllHardwareList(){
        List<HardwareTableDTO> hardwareList = hardwareService.getAllHardwareList();
        return ResponseEntity.ok().body(hardwareList);
    }

    @GetMapping("/{hardwareId}")
    public ResponseEntity<HardwareDetailDTO> getHardwareById(@PathVariable Long hardwareId) {
        HardwareDetailDTO hardwareDetailDTO = hardwareService.getHardwareById(hardwareId);
        return ResponseEntity.ok().body(hardwareDetailDTO);
    }

    @GetMapping("/{hardwareId}/reports")
    public ResponseEntity<List<ReportTableDTO>> getReportsByHardwareId(@PathVariable Long hardwareId) {
        List<ReportTableDTO> reports = hardwareService.getReportsByHardwareId(hardwareId);
        return ResponseEntity.ok().body(reports);
    }

    @PostMapping("/{hardwareId}/reports")
    public ResponseEntity<ReportHistoryDTO> createReport(@PathVariable long hardwareId, @RequestBody ReportRequestDTO reportRequestDTO) {
        ReportHistoryDTO reportHistoryDTO = reportService.saveReport(hardwareId, reportRequestDTO);
        return ResponseEntity.ok().body(reportHistoryDTO);
    }
}
