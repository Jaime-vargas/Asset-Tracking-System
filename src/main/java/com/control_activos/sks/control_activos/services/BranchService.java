package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.FileMapper;
import com.control_activos.sks.control_activos.mapper.HardwareMapper;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.FileRequestDTO;
import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.models.dto.FileEntityDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportCountDTO;
import com.control_activos.sks.control_activos.models.entity.*;
import com.control_activos.sks.control_activos.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final FileCategoryService fileCategoryService;
    private final BranchRepository branchRepository;
    private final FilesService filesService;
    private final HardwareRepository hardwareRepository;
    private final ReportRepository reportRepository;

    /** Branch services */
    @Transactional
    public BranchDTO editBranch(Long branchId, BranchDTO branchDTO) {
        Branch branch = findBranchById(branchId);
        branch.setName(branchDTO.getName());
        branch = branchRepository.save(branch);
        return Mapper.entityToDTO(branch);
    }

    /** Hardware related services */
    public List<HardwareTableDTO> getHardwareByBranchId(@PathVariable Long branchId){
        findBranchById(branchId);
        List<Hardware> hardwareList = hardwareRepository.findHardwareByBranchId(branchId);
        List<ReportCountDTO> activeReports = reportRepository.findActiveReportsByBranchId(branchId);
        return mergeHardwareAndReportsToDTO(hardwareList, activeReports);
    }
    /***********************************************************/
    /** File related services */
    public List<FileEntityDTO> getFilesByBranchId(Long branchId){
        Branch branch = findBranchById(branchId);
        List<FileEntity> files = branch.getFiles();
        return files.stream().map(FileMapper::toFileEntityDto).collect(Collectors.toList());
    }

    @Transactional
    public FileEntityDTO uploadFile(Long branchId, MultipartFile file, FileRequestDTO fileRequestDTO) {
        Branch branch = findBranchById(branchId);
        FileCategory category = fileCategoryService.findById(fileRequestDTO.getCategoryId());
        filesService.validateNotEmpty(file);
        Path projectPath = filesService.getPathOfProjectFiles(branch);
        filesService.createDirectoriesIfNotExist(projectPath);
        Path storePath = filesService.getStorePath(projectPath, file.getOriginalFilename());
        FileEntity savedFile = filesService.saveFile(file, storePath, category);
        branch.getFiles().add(savedFile);

        return FileMapper.toFileEntityDto(savedFile);
    }

    /***********************************************************/
    /** Helper methods */

    // HELPER METHODS
    private Map<Long, List<ReportCountDTO>> groupReportsById(List<ReportCountDTO> activeReports) {
        return activeReports.stream().collect(Collectors.groupingBy(ReportCountDTO::getId));
    }

    private List<HardwareTableDTO> mergeHardwareAndReportsToDTO(List<Hardware> hardwareList, List<ReportCountDTO> activeReports) {
        Map<Long, List<ReportCountDTO>> reportsByBranchId = groupReportsById(activeReports);
        return hardwareList.stream().map(hardware -> {
            HardwareTableDTO hardwareTableDTO = HardwareMapper.toHardwareTableDTO(hardware);
            hardwareTableDTO.setReportsActive(reportsByBranchId.getOrDefault(hardware.getId(), List.of()));
            return hardwareTableDTO;
        }).toList();
    }

    /** Validations */
    public Branch findBranchById(Long projectId) {
        return branchRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.BRANCH_NOT_FOUND.build(projectId)));
    }
}
