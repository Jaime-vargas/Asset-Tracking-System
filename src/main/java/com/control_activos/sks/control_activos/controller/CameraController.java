package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.CameraDTO;
import com.control_activos.sks.control_activos.services.CameraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursal")
public class CameraController {

    private CameraService cameraService;
    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @GetMapping("/{sucursalId}/camera")
    public ResponseEntity<List<CameraDTO>> getCameraDTOList (@PathVariable Long sucursalId){
        List<CameraDTO> cameraDTOList = cameraService.getCameraDTOList(sucursalId);
        return ResponseEntity.ok().body(cameraDTOList);
    }

    @PostMapping("/{sucursalId}/camera")
    public ResponseEntity<CameraDTO> saveCamera(@PathVariable Long sucursalId, @RequestBody CameraDTO cameraDTO) {
        CameraDTO savedCameraDTO = cameraService.saveCamera(sucursalId, cameraDTO);
        return ResponseEntity.ok().body(savedCameraDTO);
    }

    @PutMapping("/{sucursalId}/camera/{cameraId}")
    public ResponseEntity<CameraDTO> editCamera(@PathVariable Long sucursalId, @PathVariable Long cameraId, @RequestBody CameraDTO cameraDTO) {
        CameraDTO updatedCameraDTO = cameraService.editCamera(sucursalId, cameraId, cameraDTO);
        return ResponseEntity.ok().body(updatedCameraDTO);
    }

}
