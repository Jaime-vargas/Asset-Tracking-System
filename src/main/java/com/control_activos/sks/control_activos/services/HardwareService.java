package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.HardwareMapper;
import com.control_activos.sks.control_activos.mapper.ReportMapper;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareTableDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportCountDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportHistoryDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportTableDTO;
import com.control_activos.sks.control_activos.models.entity.Hardware;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.repository.HardwareRepository;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HardwareService {

    private final BranchService branchService;
    private final ClientService clientService;
    private final HardwareRepository hardwareRepository;
    private final ReportRepository reportRepository;

    public HardwareService(BranchService branchService, ClientService clientService, HardwareRepository hardwareRepository, ReportRepository reportRepository) {
        this.branchService = branchService;
        this.clientService = clientService;
        this.hardwareRepository = hardwareRepository;
        this.reportRepository = reportRepository;
    }

    // GET REPORTS BY HARDWARE ID
    public List<ReportTableDTO> getReportsByHardwareId(Long hardwareID) {
        findHardwareById(hardwareID);
        List<Report> reports = reportRepository.findByHardwareIdOrderByStatusDescDueDateAsc(hardwareID);
        return reports.stream().map(ReportMapper::toReportTableDTO).toList();
    }

    // GET HARDWARE BY ID
    public HardwareDetailDTO getHardwareById(Long hardwareID) {
        Hardware hardware = findHardwareById(hardwareID);
        List<Report> recentActiveReports = reportRepository.findTop4ByHardwareIdOrderByStatusDescDueDateAsc(hardwareID);
        HardwareDetailDTO hardwareDetailDTO = HardwareMapper.hardwareDetailDTO(hardware);

        // Reports count
        long activeReportsCount = hardware.getReports().stream().filter(Report::getStatus).count();
        long overdueReportsCount = hardware.getReports().stream()
                .filter(report -> report.getStatus() && report.getDueDate().isBefore(OffsetDateTime.now()))
                .count();
        hardwareDetailDTO.setActiveReportsCount(activeReportsCount);
        hardwareDetailDTO.setOverdueReportsCount(overdueReportsCount);

        List<ReportHistoryDTO> reportHistoryDTO = recentActiveReports.stream().map(ReportMapper::toReportHistoryDTO).toList();
        hardwareDetailDTO.setRecentActiveReports(reportHistoryDTO);
        return hardwareDetailDTO;
    }

    // GET HARDWARE LIST
    public List<HardwareTableDTO> getAllHardwareList() {
        List<Hardware> hardwareList = hardwareRepository.findAll();
        List <ReportCountDTO> activeReports = reportRepository.getAllActiveReportsGroupedByHardwareId();
        return mergeHardwareAndReportsToDTO(hardwareList, activeReports);
    }

    // HELPER METHODS
    private List<HardwareTableDTO> mergeHardwareAndReportsToDTO(List<Hardware> hardwareList, List<ReportCountDTO> activeReports){
        Map<Long, List<ReportCountDTO>> reportsByHardwareId = activeReports.stream().collect(
                Collectors.groupingBy(ReportCountDTO::getId));
        return hardwareList.stream().map(hardware -> {
                    HardwareTableDTO hardwareTableDTO = HardwareMapper.toHardwareTableDTO(hardware);
                    hardwareTableDTO.setReportsActive(reportsByHardwareId.getOrDefault(hardware.getId(), List.of()));
                    return hardwareTableDTO;
                }
        ).toList();
    }

    // VALIDATIONS
    public Hardware findHardwareById(Long hardwareId) {
        return hardwareRepository.findById(hardwareId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.HARDWARE_NOT_FOUND.build(hardwareId)));
    }
}
