package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.AuthenticationExceptionEnum;
import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ReportPriorityEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.AuthenticationException;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.ReportMapper;
import com.control_activos.sks.control_activos.models.dto.reportDTO.*;
import com.control_activos.sks.control_activos.models.entity.Hardware;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.servlet.filter.OrderedFormContentFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final HardwareService hardwareService;
    private final UserEntityService userEntityService;
    private final OrderedFormContentFilter orderedFormContentFilter;

    public List<ReportTableDTO> getAllReports(){
        List<Report> reports = reportRepository.findAllByOrderByStatusDescDueDateAsc();
        return reports.stream().map(ReportMapper::toReportTableDTO).toList();
    }

    public ReportDetailDTO getReportDetail(Long reportId) {
        Report report = findReportById(reportId);
        return ReportMapper.toReportDetailDTO(report);
    }

    @Transactional
    public void closeReport (Long reportId) {
        Report report = findReportById(reportId);
        report.setStatus(false);
        report.setClosedAt(OffsetDateTime.now());
        reportRepository.save(report);
    }

    @Transactional
    public ReportHistoryDTO saveReport(Long hardwareId, ReportRequestDTO reportRequestDTO) {
        Hardware hardware = hardwareService.findHardwareById(hardwareId);

        ReportPriorityEnum reportPriority = validatePriorityEnum(reportRequestDTO.getPriorityEnum());
        OffsetDateTime dueDate = validateDueDate(reportRequestDTO.getDueDate(), OffsetDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userEntityService.findByUserEntityByUsername(username);

        Report report = new Report();
        report.setTitle(reportRequestDTO.getTitle());
        report.setPriority(reportPriority);
        report.setReportDetails(reportRequestDTO.getReportDetails());
        report.setDueDate(dueDate);
        report.setHardware(hardware);
        report.setReportedBy(user);
        report.setCreatedAt(OffsetDateTime.now());
        report.setUpdatedAt(OffsetDateTime.now());
        report.setStatus(true);

        report = reportRepository.save(report);
        return ReportMapper.toReportHistoryDTO(report);
    }

    @Transactional
    public ReportResponseDTO updateReport(Long reportId, ReportRequestDTO reportRequestDTO) {
        Report report = findReportById(reportId);
        ReportPriorityEnum reportPriority = validatePriorityEnum(reportRequestDTO.getPriorityEnum());
        OffsetDateTime dueDate = validateDueDate(reportRequestDTO.getDueDate(), report.getCreatedAt());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED,
                    AuthenticationExceptionEnum.TOKEN_EXPIRED.getMessage());
        }
        String username = authentication.getName();
        UserEntity user = userEntityService.findByUserEntityByUsername(username);
        if(!user.getUsername().equals(username)){
            throw new AuthenticationException(HttpStatus.FORBIDDEN,
                    AuthenticationExceptionEnum.FORBIDDEN_ACCESS.getMessage());
        }

        report.setTitle(reportRequestDTO.getTitle());
        report.setPriority(reportPriority);
        report.setReportDetails(reportRequestDTO.getReportDetails());
        report.setDueDate(dueDate);
        report.setUpdatedAt(OffsetDateTime.now());

        return ReportMapper.toReportResponseDTO(report);
    }


    private OffsetDateTime validateDueDate(String dueDate, OffsetDateTime creationDate) {
        OffsetDateTime validatedDueDate;
        try {
            validatedDueDate = OffsetDateTime.parse(dueDate);
            if(validatedDueDate.isAfter(creationDate)) {
                return validatedDueDate;
            } else {
                throw new OperationNotAllowedException(
                        OperationNotAllowedExceptionEnum.INVALID_DUE_DATE.getMessage());
            }
        } catch (DateTimeParseException e) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.INVALID_DUE_DATE_FORMAT.getMessage());
        }
    }

    public Report findReportById(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.REPORT_NOT_FOUND.build(reportId)));
    }

    public void validateReportIsOpen(Report report) {
        if (!report.getStatus()) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.REPORT_ALREADY_CLOSED.getMessage());
        }
    }

    public ReportPriorityEnum validatePriorityEnum(String priority) {
        try{
            return ReportPriorityEnum.valueOf(priority);
        } catch (IllegalArgumentException e) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.INVALID_REPORT_PRIORITY.getMessage());
        }
    }
}
