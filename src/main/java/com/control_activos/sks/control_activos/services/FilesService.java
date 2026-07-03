package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.CameraPhotoUploads;
import com.control_activos.sks.control_activos.enums.FileEnum;
import com.control_activos.sks.control_activos.exception.FileException;
import com.control_activos.sks.control_activos.mapper.ClientMapper;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.models.entity.Photo;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.repository.PhotoRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class FilesService {

    private final CameraService cameraService;
    private final ClientService clientService;
    private final PhotoRepository photoRepository;
    private final ReportService reportService;
    private final HardwareService hardwareService;


    public FilesService(CameraService cameraService, ClientService clientService, PhotoRepository photoRepository, ReportService reportService, HardwareService hardwareService) {
        this.cameraService = cameraService;
        this.clientService = clientService;
        this.photoRepository = photoRepository;
        this.reportService = reportService;
        this.hardwareService = hardwareService;
    }

    @Transactional
    public ClientDTO UploadClientPhoto(Long clientId, MultipartFile file, Boolean replaceExisting) {
        Client client = clientService.findClientById(clientId);
        validateIsImage(file);

        Photo currentPhoto = client.getPhoto();
        if (currentPhoto != null && !replaceExisting) {
            throw new FileException(
                    FileEnum.ALREADY_EXISTS.getMessage(" " + file.getOriginalFilename() + " for client with ID: " + client.getId())
            );
        }

        Path path = getPathOfClient(clientId);
        createDirectoriesIfNotExist(path);
        Path storePath = getStorePath(path, file.getOriginalFilename());

        if(replaceExisting && currentPhoto != null && !Files.exists(storePath)) {
            deleteFile(Path.of(currentPhoto.getFilePath()));
            photoRepository.delete(currentPhoto);
        }

        client.setPhoto(saveFileToPath(file, storePath));
        return ClientMapper.toClientDTO(client);
    }

    @Transactional
    public HardwareDetailDTO uploadCameraPhoto(Long hardwareID, MultipartFile file,
                                               CameraPhotoUploads photoType, Boolean replaceExisting) {
        Camera camera = cameraService.findCameraById(hardwareID);

        validateIsImage(file);

        Photo currentPhoto = switch (photoType) {
            case VIEW_FROM_CAMERA -> camera.getViewFromCameraPhoto();
            case VIEW_TO_CAMERA -> camera.getViewToCameraPhoto();
        };
        if (currentPhoto != null && !replaceExisting) {
            throw new FileException(
                    FileEnum.ALREADY_EXISTS.getMessage(" " + file.getOriginalFilename() + " for camera with ID: " + hardwareID)
            );
        }

        Path path = getPathOfCamera(camera);
        createDirectoriesIfNotExist(path);
        Path storePath = getStorePath(path, file.getOriginalFilename());

        if(replaceExisting && currentPhoto != null && !Files.exists(storePath)) {
            deleteFile(Path.of(currentPhoto.getFilePath()));
            photoRepository.delete(currentPhoto);
        }

        switch (photoType) {
            case VIEW_FROM_CAMERA -> camera.setViewFromCameraPhoto(saveFileToPath(file, storePath));
            case VIEW_TO_CAMERA -> camera.setViewToCameraPhoto(saveFileToPath(file, storePath));
        }
        return hardwareService.getHardwareById(hardwareID);
    }

    @Transactional
    public void uploadPhotoToReport(Long reportId, MultipartFile file) {
        Report report = reportService.findReportById(reportId);
        reportService.validateReportIsOpen(report);

        validateIsImage(file);

        Path path = getPathOfReport(report);
        createDirectoriesIfNotExist(path);

        Path storePath = getStorePath(path, file.getOriginalFilename());
        report.setUpdatedAt(OffsetDateTime.now());
        report.getPhotos().add(saveFileToPath(file, storePath));
    }

    private void validateIsImage(MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new FileException(FileEnum.IMAGE_FORMAT_ERROR.getMessage());
        }
    }

    private void createDirectoriesIfNotExist(Path savePath){
        try {
            Files.createDirectories(savePath);
        }catch (IOException e) {
            throw new FileException(FileEnum.DIRECTORY_CREATION_ERROR.getMessage(savePath.toString()));
        }
    }

    private Path getPathOfClient(Long clientId) {
        return Path.of ("uploads", "Client-" + clientId);
    }

    private Path getPathOfReport(Report report) {
        return Path.of ("uploads",
                "Client-" + report.getHardware().getBranch().getClient().getId(),
                "Branch-" + report.getHardware().getBranch().getId(),
                "Hardware-" + report.getHardware().getId(),
                "Reports",  "Report-" + report.getId());
    }

    private Path getPathOfCamera(Camera camera) {
        return Path.of ("uploads",
                "Client-" + camera.getBranch().getClient().getId(),
                "Branch-" + camera.getBranch().getId(),
                "Hardware-" + camera.getId());
    }

    private Path getStorePath(Path path, String fileName) {
        fileName = fileName.trim().replaceAll("[^a-zA-Z0-9-_.]", "_");
        return path.resolve(fileName);
    }

    private Photo saveFileToPath(MultipartFile file, Path storePath) {
        if(Files.exists(storePath)) {
            throw new FileException(FileEnum.DUPLICATE_FILE.getMessage(" " + file.getOriginalFilename()));
        }else {
            try {
                file.transferTo(storePath);
                Photo photo = new Photo(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        storePath.toString().replaceAll("\\\\", "/"),
                        OffsetDateTime.now());
                return photoRepository.save(photo);
            } catch (IOException e) {
                throw new FileException(FileEnum.SAVE_ERROR.getMessage());
            }
        }
    }

    private void deleteFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileException(FileEnum.SAVE_ERROR.getMessage(" Could not delete the old file at path: " + filePath.toString()));
        }
    }
}
