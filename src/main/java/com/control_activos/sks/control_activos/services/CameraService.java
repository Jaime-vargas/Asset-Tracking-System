package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.DuplicateResourceExceptionEnum;
import com.control_activos.sks.control_activos.exception.DuplicatedResourceException;
import com.control_activos.sks.control_activos.exception.NotFoundResourceException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.CameraDTO;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.models.entity.Sucursal;
import com.control_activos.sks.control_activos.repository.CameraRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraService {

    private final CameraRepository cameraRepository;
    private final formatDataValidationService formatDataValidationService;
    private final SucursalService sucursalService;
    public CameraService(CameraRepository cameraRepository, formatDataValidationService formatDataValidationService, SucursalService sucursalService) {
        this.cameraRepository = cameraRepository;
        this.formatDataValidationService = formatDataValidationService;
        this.sucursalService = sucursalService;
    }

    public List<CameraDTO> getCameraDTOList (Long sucursalId){
        List<Camera> cameraList = cameraRepository.findBySucursalId(sucursalId);
        return cameraList.stream().map(Mapper::entityToDTO).toList();

    }
    @Transactional
    public CameraDTO saveCamera(Long sucursalId, CameraDTO cameraDTO) {
        formatDataValidation(cameraDTO);
        Sucursal sucursal = sucursalService.findSucursalById(sucursalId);
        validateDuplicateData(sucursal.getId(), cameraDTO, null);
        Camera camera = new Camera();
        setDataToEntity(sucursal, camera, cameraDTO);
        camera = cameraRepository.save(camera);
        return Mapper.entityToDTO(camera);
    }

    @Transactional
    public CameraDTO editCamera(Long sucursalId, Long cameraId, CameraDTO cameraDTO) {
        formatDataValidation(cameraDTO);
        Camera camera = findCameraById(cameraId);
        Sucursal sucursal = sucursalService.findSucursalById(sucursalId);

        /* #TODO : Uncomment to validate camera belongs to sucursal
        if (!camera.getSucursal().getId().equals(sucursalId)) {
            throw new IllegalOperationException("La cámara no pertenece a esta sucursal");
        }*/

        validateDuplicateData(sucursal.getId(), cameraDTO, camera.getId());
        setDataToEntity(sucursal, camera, cameraDTO);
        camera = cameraRepository.save(camera);
        return Mapper.entityToDTO(camera);
    }

    public void setDataToEntity(Sucursal sucursal, Camera camera, CameraDTO cameraDTO) {
        camera.setName(cameraDTO.getName());
        camera.setSerialNumber(cameraDTO.getSerialNumber());
        camera.setModel(cameraDTO.getModel());
        camera.setLocation(cameraDTO.getLocation());
        camera.setSucursal(sucursal);
        camera.setCameraId(cameraDTO.getCameraId());
        camera.setMacAddress(cameraDTO.getMacAddress());
        camera.setIpAddress(cameraDTO.getIpAddress());
    }

    public Camera findCameraById(Long cameraId) {
        return cameraRepository.findById(cameraId)
                .orElseThrow(() -> new NotFoundResourceException("Camara no encontrada")); // #TODO: Custom Exception with Enum
    }

    public void formatDataValidation(CameraDTO cameraDTO) {
        cameraDTO.setMacAddress(formatDataValidationService.validateMacAddressFormat(cameraDTO.getMacAddress()));
        cameraDTO.setIpAddress(formatDataValidationService.validateIpAddressFormat(cameraDTO.getIpAddress()));
    }

    public void validateDuplicateData(Long sucursalId, CameraDTO cameraDTO, Long currentCameraId) {
        if (cameraRepository.existsByCameraIdAndSucursalIdAndIdNot(cameraDTO.getCameraId(), sucursalId, currentCameraId)) {
            throw new DuplicatedResourceException(DuplicateResourceExceptionEnum
                    .DUPLICATE_CAMERA_ID_EXCEPTION.getMessage(cameraDTO.getCameraId()));
        }
        if (cameraRepository.existsByNameAndSucursalIdAndIdNot(cameraDTO.getName(), sucursalId, currentCameraId)) {
            throw new DuplicatedResourceException(DuplicateResourceExceptionEnum
                    .DUPLICATE_CAMERA_NAME_EXCEPTION.getMessage(cameraDTO.getName()));
        }
        if (cameraRepository.existsBySerialNumberAndSucursalIdAndIdNot(cameraDTO.getSerialNumber(), sucursalId, currentCameraId)) {
            throw new DuplicatedResourceException(DuplicateResourceExceptionEnum
                    .DUPLICATE_CAMERA_SERIAL_NUMBER_EXCEPTION.getMessage(cameraDTO.getSerialNumber()));
        }
        if (cameraRepository.existsByMacAddressAndSucursalIdAndIdNot(cameraDTO.getMacAddress(), sucursalId, currentCameraId)) {
            throw new DuplicatedResourceException(DuplicateResourceExceptionEnum
                    .DUPLICATE_CAMERA_MAC_ADDRESS_EXCEPTION.getMessage(cameraDTO.getMacAddress()));
        }
        if (cameraRepository.existsByIpAddressAndSucursalIdAndIdNot(cameraDTO.getIpAddress(), sucursalId, currentCameraId)) {
            throw new DuplicatedResourceException(DuplicateResourceExceptionEnum
                    .DUPLICATE_CAMERA_IP_ADDRESS_EXCEPTION.getMessage(cameraDTO.getIpAddress()));
        }
    }
    }


