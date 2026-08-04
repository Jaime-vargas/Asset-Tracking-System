package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.FileRequestDTO;
import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.dto.ImportResponse;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditResponseDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.services.BranchXLSService;
import com.control_activos.sks.control_activos.services.BranchService;
import com.control_activos.sks.control_activos.services.CameraService;
import com.control_activos.sks.control_activos.services.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchXLSService branchXLSService;
    private final CameraService cameraService;
    private final FilesService filesService;

    /** Branch Endpoints */
    @PutMapping("/{branchId}")
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable Long branchId, @RequestBody BranchDTO branchDTO) {
        branchDTO = branchService.editBranch(branchId, branchDTO);
        return ResponseEntity.ok().body(branchDTO);
    }

    /** Hardware related endpoints  */
    @GetMapping("/{branchId}/hardware")
    public ResponseEntity<List<HardwareTableDTO>> getHardwareByBranchId(@PathVariable Long branchId){
        List<HardwareTableDTO> hardwareTableDTO = branchService.getHardwareByBranchId(branchId);
        return ResponseEntity.ok().body(hardwareTableDTO);
    }

    @PostMapping("/{branchId}/hardware/camera")
    public ResponseEntity<CameraEditResponseDTO> saveCamera(@PathVariable Long branchId, @RequestBody CameraEditRequestDTO cameraEditRequestDTO){
        CameraEditResponseDTO camera = cameraService.saveCamera(branchId, cameraEditRequestDTO);
        return ResponseEntity.ok().body(camera);
    }

    /** Import and Export Endpoints */
    @GetMapping("/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        byte[] template = branchXLSService.generateImportTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=camera_import_template.xlsx");
        return ResponseEntity.ok().headers(headers).body(template);
    }

    @GetMapping("/{branchId}/export-cameras")
    public ResponseEntity<byte[]> exportCamerasToXLS(@PathVariable Long branchId) {
        Branch branch = branchService.findBranchById(branchId);
        byte[] document = branchXLSService.exportCamerasToXLS(branchId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cameras_" + branch.getName() + "_" + branch.getClient().getName() + ".xlsx");
        System.out.println("disposition: attachment; filename=cameras_" + branch.getName() + "_" + branch.getClient().getName() + ".xlsx");
        return  ResponseEntity.ok().headers(headers).body(document);
    }

    @PostMapping("/{branchId}/import-cameras")
    public ResponseEntity<ImportResponse> importCameras(@PathVariable Long branchId , @RequestPart("file") MultipartFile file) {
        ImportResponse importResponse = branchXLSService.importCamerasFromXLSX(branchId, file);
        return ResponseEntity.ok().body(importResponse);
    }

    /** Files Endpoint */
    @GetMapping("/{branchId}/files")
    public ResponseEntity<List<FileEntityDTO>> getFilesByBranchId(@PathVariable Long branchId){
        List<FileEntityDTO> files = branchService.getFilesByBranchId(branchId);
        return ResponseEntity.ok().body(files);
    }

    @PostMapping("/{branchId}/files")
    public ResponseEntity<FileEntityDTO> uploadFile (@PathVariable Long branchId, @RequestPart("file") MultipartFile file, @RequestPart("data") FileRequestDTO fileRequestDTO) {
        FileEntityDTO savedFile = branchService.uploadFile(branchId, file, fileRequestDTO);
        return ResponseEntity.ok().body(savedFile);
    }
}
