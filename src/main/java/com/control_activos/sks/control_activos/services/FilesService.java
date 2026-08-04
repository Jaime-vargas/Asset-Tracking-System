package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.CameraPhotoUploads;
import com.control_activos.sks.control_activos.enums.FileExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.FileException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.ClientMapper;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.entity.*;
import com.control_activos.sks.control_activos.repository.FileRepository;

import com.control_activos.sks.control_activos.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final CameraService cameraService;
    private final ClientService clientService;
    private final FileRepository fileRepository;
    private final ReportRepository reportRepository;
    private final HardwareService hardwareService;
    private final UserEntityService userEntityService;
    @Value("${app.storage.base-path}")
    private String storagePath;

    @Transactional
    public ClientDTO UploadClientPhoto(Long clientId, MultipartFile file, Boolean replaceExisting) {
        Client client = clientService.findClientById(clientId);
        validateIsImage(file);

        FileEntity currentFileEntity = client.getPhoto();
        if (currentFileEntity != null && !replaceExisting) {
            throw new FileException(
                    FileExceptionEnum.ALREADY_EXISTS.getMessage(" " + file.getOriginalFilename() + " for client with ID: " + client.getId())
            );
        }
        Path path = getPathOfClient(clientId);
        createDirectoriesIfNotExist(path);
        Path storePath = getStorePath(path, file.getOriginalFilename());

        if(replaceExisting && currentFileEntity != null && !Files.exists(storePath)) {
            deleteFile(Path.of(currentFileEntity.getFilePath()));
            client.setPhoto(null);
            fileRepository.delete(currentFileEntity);
        }

        client.setPhoto(saveFileToPath(file, storePath));
        return ClientMapper.toClientDTO(client);
    }

    @Transactional
    public HardwareDetailDTO uploadCameraPhoto(Long hardwareID, MultipartFile file,
                                               CameraPhotoUploads photoType, Boolean replaceExisting) {
        Camera camera = cameraService.findCameraById(hardwareID);

        validateIsImage(file);

        FileEntity currentFileEntity = switch (photoType) {
            case VIEW_FROM_CAMERA -> camera.getViewFromCameraFileEntity();
            case VIEW_TO_CAMERA -> camera.getViewToCameraFileEntity();
        };
        if (currentFileEntity != null && !replaceExisting) {
            throw new FileException(
                    FileExceptionEnum.ALREADY_EXISTS.getMessage(" " + file.getOriginalFilename() + " for camera with ID: " + hardwareID)
            );
        }

        Path path = getPathOfCamera(camera);
        createDirectoriesIfNotExist(path);
        Path storePath = getStorePath(path, file.getOriginalFilename());

        if(replaceExisting && currentFileEntity != null && !Files.exists(storePath)) {
            switch (photoType) {
                case VIEW_FROM_CAMERA -> camera.setViewFromCameraFileEntity(null);
                case VIEW_TO_CAMERA -> camera.setViewToCameraFileEntity(null);
            };
            deleteFile(Path.of(currentFileEntity.getFilePath()));
            fileRepository.delete(currentFileEntity);
        }

        switch (photoType) {
            case VIEW_FROM_CAMERA -> camera.setViewFromCameraFileEntity(saveFileToPath(file, storePath));
            case VIEW_TO_CAMERA -> camera.setViewToCameraFileEntity(saveFileToPath(file, storePath));
        }
        return hardwareService.getHardwareById(hardwareID);
    }

    @Transactional
    public void uploadPhotoToReport(Long reportId, MultipartFile file) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.REPORT_NOT_FOUND.build(reportId)));

        // TODO. FIX this entire class to get order
        // reportRepository.validateReportIsOpen(report);

        validateIsImage(file);

        Path path = getPathOfReport(report);
        createDirectoriesIfNotExist(path);

        Path storePath = getStorePath(path, file.getOriginalFilename());
        report.setUpdatedAt(OffsetDateTime.now());
        report.getFileEntities().add(saveFileToPath(file, storePath));
    }

    @Transactional
    public void deletePhotoFromReport(Long reportId, Long photoId) {
        Report report = reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.REPORT_NOT_FOUND.build(reportId)));
        // TODO. FIX this entire class to get order
        // reportRepository.validateReportIsOpen(report);
        FileEntity fileEntity = report.getFileEntities().stream().filter(p -> p.getId().equals(photoId)).findFirst().orElseThrow(()->
                new FileException(FileExceptionEnum.FILE_NOT_FOUND.getMessage(" for photo with ID: " + photoId + " in report with ID: " + reportId)));
        deleteFile(Path.of(fileEntity.getFilePath()));
        fileRepository.delete(fileEntity);
        report.getFileEntities().remove(fileEntity);
        report.setUpdatedAt(OffsetDateTime.now());
    }

    private void validateIsImage(MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new FileException(FileExceptionEnum.IMAGE_FORMAT_ERROR.getMessage());
        }
    }

    public void validateNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(FileExceptionEnum.INVALID_FILE.getMessage());
        }
    }


    public void createDirectoriesIfNotExist(Path savePath){
        try {
            Files.createDirectories(savePath);
        }catch (IOException e) {
            throw new FileException(FileExceptionEnum.DIRECTORY_CREATION_ERROR.getMessage(savePath.toString()));
        }
    }

    private Path getPathOfClient(Long clientId) {
        return Path.of (storagePath, "Client-" + clientId);
    }

    public Path getPathOfProjectFiles(Branch project) {
        return Path.of (storagePath,
                "Client-" + project.getClient().getId(),
                "Project-" + project.getId(),
                "Files"
                );
    }

    private Path getPathOfReport(Report report) {
        return Path.of (storagePath,
                "Client-" + report.getHardware().getBranch().getClient().getId(),
                "Project-" + report.getHardware().getBranch().getId(),
                "Hardware-" + report.getHardware().getId(),
                "Reports",  "Report-" + report.getId());
    }

    private Path getPathOfCamera(Camera camera) {
        return Path.of (storagePath,
                "Client-" + camera.getBranch().getClient().getId(),
                "Project-" + camera.getBranch().getId(),
                "Hardware-" + camera.getId());
    }

    public Path getStorePath(Path path, String fileName) {
        fileName = fileName.trim().replaceAll("[^a-zA-Z0-9-_.]", "_");
        return path.resolve(fileName);
    }

    /* TODO. VERIFY ALL FUNCTIONS ON THIS CLASS */
    /* NEW **************************************/

    public FileEntity saveFile(MultipartFile file, Path storePath, FileCategory fileCategory) {
        if (Files.exists(storePath)) {
            throw new FileException(FileExceptionEnum.DUPLICATE_FILE.getMessage(" " + file.getOriginalFilename()));
        }
        try {
            UserEntity currentUser = userEntityService.authenticateCurrentUser();
            file.transferTo(storePath);
            Path relative = Path.of(storagePath).relativize(storePath);

            System.out.println("Relative : " + relative);
            System.out.println("FilePath : " + storePath);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setFilePath("uploads/" + relative.toString().replace("\\", "/"));
            fileEntity.setUploadedAt(OffsetDateTime.now());
            fileEntity.setCategory(fileCategory);
            fileEntity.setUser(currentUser);
            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new FileException(FileExceptionEnum.SAVE_ERROR.getMessage());
        }
    }
        /* ******************************************/

    private FileEntity saveFileToPath(MultipartFile file, Path storePath) {
        if(Files.exists(storePath)) {
            throw new FileException(FileExceptionEnum.DUPLICATE_FILE.getMessage(" " + file.getOriginalFilename()));
        }else {
            try {
                UserEntity currentUser = userEntityService.authenticateCurrentUser();
                file.transferTo(storePath);

                Path relative = Path.of(storagePath).relativize(storePath);

                FileEntity fileEntity = new FileEntity(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        "uploads/" + relative.toString().replace("\\", "/"),
                        OffsetDateTime.now());
                fileEntity.setUser(currentUser);
                return fileRepository.save(fileEntity);
            } catch (IOException e) {
                throw new FileException(FileExceptionEnum.SAVE_ERROR.getMessage());
            }
        }
    }

    private void deleteFile(Path filePath) {
        try {
            String relativePath = filePath.toString()
                    .replace("uploads\\", "")
                    .replace("uploads/", "");

            Path target = Path.of(storagePath).resolve(relativePath);

            Files.deleteIfExists(target);
            System.out.println("FILEPATH: " + target);
        } catch (Exception e) {
            throw new FileException(FileExceptionEnum.SAVE_ERROR.getMessage(" Could not delete the old file at path: " + e.getMessage()));
        }
    }
}
