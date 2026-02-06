package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.ReportDTO;
import com.control_activos.sks.control_activos.models.entity.Hardware;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.HardwareRepository;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import com.control_activos.sks.control_activos.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ReportService {

    private final HardwareRepository hardwareRepository;
    private final ReportRepository reportRepository;
    public ReportService(HardwareRepository hardwareRepository, ReportRepository reportRepository) {
        this.hardwareRepository = hardwareRepository;
        this.reportRepository = reportRepository;
    }

    // #TODO set real user in report
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Transactional
    public ReportDTO saveReport(Long hardwareId, ReportDTO reportDTO) {

        UserEntity userEntity = userEntityRepository.findById(1L).get();

        Hardware hardware = hardwareRepository.findById(hardwareId).get();

        Report report = new Report();
        report.setTitle(reportDTO.getTitle());
        report.setActive(true);
        report.setHardware(hardware);
        hardware.setLastMaintenanceDate(OffsetDateTime.now());
        report.setCreatedAt(OffsetDateTime.now()); // updated dado of camera
        report.setReportedBy(userEntity); // #TODO set real user in report
        report = reportRepository.save(report);
        return Mapper.entityToDTO(report);
    }

    @Transactional
    public void closeReport (Long hardwareId, Long reportId) {
        Report report = findReportById(reportId);
        if(!report.getHardware().getId().equals(hardwareId)) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.REPORT_NOT_BELONG_TO_HARDWARE.getMessage());
        }
        report.setActive(false);
        report.setClosedAt(OffsetDateTime.now());
        reportRepository.save(report);
    }


    public Report findReportById(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(
                () -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.REPORT_NOT_FOUND.build(reportId)));
    }
}
