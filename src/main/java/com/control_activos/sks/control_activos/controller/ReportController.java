package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.ReportDTO;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hardware/{hardwareId}/report")
public class ReportController {

    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@PathVariable long hardwareId, @RequestBody ReportDTO reportDTO) {
        reportDTO = reportService.saveReport(hardwareId, reportDTO);
        return ResponseEntity.ok().body(reportDTO);
    }

    @PutMapping("/{reportId}/close")
    public ResponseEntity<?> closeReport (@PathVariable long hardwareId, @PathVariable long reportId) {
        reportService.closeReport(hardwareId, reportId);
        return  ResponseEntity.noContent().build();
    }
}
