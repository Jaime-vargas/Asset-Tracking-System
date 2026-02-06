package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.CameraDTO;
import com.control_activos.sks.control_activos.services.CameraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursals")
public class CameraController {

    private final CameraService cameraService;
    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @GetMapping("/{sucursalId}/cameras")
    public ResponseEntity<List<CameraDTO>> getCameraDTOList (@PathVariable Long sucursalId){
        List<CameraDTO> cameraDTOList = cameraService.getCameraDTOList(sucursalId);
        return ResponseEntity.ok().body(cameraDTOList);
    }

    @PostMapping("/{sucursalId}/cameras")
    public ResponseEntity<CameraDTO> saveCamera(@PathVariable Long sucursalId, @RequestBody CameraDTO cameraDTO) {
        CameraDTO savedCameraDTO = cameraService.saveCamera(sucursalId, cameraDTO);
        return ResponseEntity.ok().body(savedCameraDTO);
    }

    @PutMapping("/{sucursalId}/cameras/{cameraId}")
    public ResponseEntity<CameraDTO> editCamera(@PathVariable Long sucursalId, @PathVariable Long cameraId, @RequestBody CameraDTO cameraDTO) {
        CameraDTO updatedCameraDTO = cameraService.editCamera(sucursalId, cameraId, cameraDTO);
        return ResponseEntity.ok().body(updatedCameraDTO);
    }

}
