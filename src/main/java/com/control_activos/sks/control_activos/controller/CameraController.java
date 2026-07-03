package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.enums.CameraPhotoUploads;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditResponseDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.services.CameraService;
import com.control_activos.sks.control_activos.services.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/hardware/{cameraId}/camera")
public class CameraController {

    private final CameraService cameraService;
    private final FilesService filesService;
    public CameraController(CameraService cameraService, FilesService filesService) {
        this.filesService = filesService;
        this.cameraService = cameraService;
    }

    @GetMapping
    public ResponseEntity<CameraEditResponseDTO> getCameraEditData(@PathVariable Long cameraId) {
        CameraEditResponseDTO camera = cameraService.getCameraEditData(cameraId);
        return ResponseEntity.ok().body(camera);
    }

    @PutMapping
    public ResponseEntity<CameraEditResponseDTO> updateCamera(@PathVariable Long cameraId, @RequestBody CameraEditRequestDTO cameraEditRequestDTO) {
        CameraEditResponseDTO camera = cameraService.updateCamera(cameraId, cameraEditRequestDTO);
        return ResponseEntity.ok().body(camera);

    }

    @PostMapping("/photos")
    public ResponseEntity<HardwareDetailDTO> addPhoto(@PathVariable Long cameraId, @RequestPart("file") MultipartFile file,
                                      @RequestParam CameraPhotoUploads photoType,
                                      @RequestParam(defaultValue = "false") Boolean replaceExisting) {
        HardwareDetailDTO hardwareDetailDTO = filesService.uploadCameraPhoto(cameraId, file, photoType, replaceExisting);
        return ResponseEntity.ok().body(hardwareDetailDTO);
    }

    /*
    @GetMapping("/{branchId}/cameras")
    public ResponseEntity<List<CameraDetailDTO>> getCameraDTOList (@PathVariable Long branchId){
        List<CameraDetailDTO> cameraDetailDTOList = cameraService.getCameraDTOList(branchId);
        return ResponseEntity.ok().body(cameraDetailDTOList);
    }

    @PostMapping("/{branchId}/cameras")
    public ResponseEntity<CameraDetailDTO> saveCamera(@PathVariable Long branchId, @RequestBody CameraDetailDTO cameraDetailDTO) {
        CameraDetailDTO savedCameraDetailDTO = cameraService.saveCamera(branchId, cameraDetailDTO);
        return ResponseEntity.ok().body(savedCameraDetailDTO);
    }

    @PutMapping("/{branchId}/cameras/{cameraId}")
    public ResponseEntity<CameraDetailDTO> editCamera(@PathVariable Long branchId, @PathVariable Long cameraId, @RequestBody CameraDetailDTO cameraDetailDTO) {
        CameraDetailDTO updatedCameraDetailDTO = cameraService.editCamera(branchId, cameraId, cameraDetailDTO);
        return ResponseEntity.ok().body(updatedCameraDetailDTO);
    }
 */
}
