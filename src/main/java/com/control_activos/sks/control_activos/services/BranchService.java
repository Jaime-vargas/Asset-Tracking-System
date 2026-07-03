package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.BranchMapper;
import com.control_activos.sks.control_activos.mapper.HardwareMapper;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.models.dto.branchDTO.BranchTableDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportCountDTO;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Hardware;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.repository.BranchRepository;
import com.control_activos.sks.control_activos.repository.HardwareRepository;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final ClientService clientService;
    private final BranchRepository branchRepository;
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


    /** Helper methods */
    @Transactional
    public BranchDTO saveBranch(Long clientId, BranchDTO branchDTO) {
        Client client = clientService.findClientById(clientId);
        Branch branch = new Branch();
        branch.setClient(client);
        branch.setName(branchDTO.getName());
        branch = branchRepository.save(branch);
        return Mapper.entityToDTO(branch);
    }

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
    public Branch findBranchById(Long sucursalId) {
        return branchRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.BRANCH_NOT_FOUND.build(sucursalId)));
    }
}
