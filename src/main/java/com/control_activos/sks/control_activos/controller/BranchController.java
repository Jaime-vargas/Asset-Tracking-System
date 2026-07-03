package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditResponseDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.services.BranchService;
import com.control_activos.sks.control_activos.services.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final CameraService cameraService;


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

}
