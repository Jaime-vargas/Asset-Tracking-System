package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.CommentDTO;
import com.control_activos.sks.control_activos.models.entity.Comment;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FormatDataValidationService formatDataValidationService;
    private final ReportService reportService;
    public  CommentService(CommentRepository commentRepository, FormatDataValidationService formatDataValidationService, ReportService reportService) {
        this.commentRepository = commentRepository;
        this.formatDataValidationService = formatDataValidationService;
        this.reportService = reportService;
    }

    @Transactional
    public CommentDTO saveComment(Long reportId, CommentDTO commentDTO) {
        Report report = reportService.findReportById(reportId);
        checkIfValid(report.getActive());
        Comment comment = new Comment();
        comment.setText(formatDataValidationService.lowerCase(commentDTO.getText()));
        comment = commentRepository.save(comment);
        return Mapper.entityToDTO(comment);
    }

    public void checkIfValid(Boolean valid) {
        if (!valid) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.REPORT_ALREADY_CLOSED.getMessage()
            );
        }
    }

}
