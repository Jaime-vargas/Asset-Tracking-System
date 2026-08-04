package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.reportDTO.*;
import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentDTO;
import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentRequestDTO;
import com.control_activos.sks.control_activos.services.CommentService;
import com.control_activos.sks.control_activos.services.FilesService;
import com.control_activos.sks.control_activos.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final CommentService commentService;
    private final ReportService reportService;
    private final FilesService filesService;

    public ReportController(CommentService commentService, ReportService reportService, FilesService filesService) {
        this.commentService = commentService;
        this.reportService = reportService;
        this.filesService = filesService;
    }

    @GetMapping
    public ResponseEntity<List<ReportTableDTO>> getAllReports() {
        List<ReportTableDTO> reportTableDTO = reportService.getAllReports();
        return ResponseEntity.ok().body(reportTableDTO);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailDTO> getReportDetail(@PathVariable long reportId) {
        ReportDetailDTO reportDetailDTO = reportService.getReportDetail(reportId);
        return ResponseEntity.ok().body(reportDetailDTO);
    }

    @PostMapping("/{reportId}/comments")
    public ResponseEntity<CommentDTO> saveComment(@PathVariable Long reportId, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentDTO commentDTO = commentService.saveComment(reportId, commentRequestDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportResponseDTO> updateReport(@PathVariable long reportId, @RequestBody ReportRequestDTO reportRequestDTO) {
        ReportResponseDTO updatedReport = reportService.updateReport(reportId, reportRequestDTO);
        return ResponseEntity.ok().body(updatedReport);
    }

    @PutMapping("/{reportId}/close")
    public ResponseEntity<?> closeReport (@PathVariable long reportId) {
        reportService.closeReport(reportId);
        return  ResponseEntity.noContent().build();
    }

    // UPLOAD PHOTOS
    @PostMapping("/{reportId}/photos")
    public ResponseEntity<?> addPhoto(@PathVariable Long reportId, @RequestPart("file") MultipartFile file) {
        filesService.uploadPhotoToReport(reportId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reportId}/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long reportId, @PathVariable Long photoId) {
        filesService.deletePhotoFromReport(reportId, photoId);
        return ResponseEntity.noContent().build();
    }
}
