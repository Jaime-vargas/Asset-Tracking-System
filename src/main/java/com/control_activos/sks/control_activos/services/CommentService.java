package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.CommentDTO;
import com.control_activos.sks.control_activos.models.entity.Comment;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class CommentService {

    @Autowired
    private UserEntityService userEntityService;


    private final CommentRepository commentRepository;
    private final FormatDataValidationService formatDataValidationService;
    private final ReportService reportService;
    public CommentService(CommentRepository commentRepository, FormatDataValidationService formatDataValidationService, ReportService reportService) {
        this.commentRepository = commentRepository;
        this.formatDataValidationService = formatDataValidationService;
        this.reportService = reportService;
    }

    @Transactional
    public CommentDTO saveComment(Long reportId, CommentDTO commentDTO) {
        Report report = reportService.findReportById(reportId);
        checkIfValid(report.getActive());
        report.setUpdatedAt(OffsetDateTime.now());
        Comment comment = new Comment();
        comment.setText(formatDataValidationService.lowerCase(commentDTO.getText()));
        UserEntity user = userEntityService.findByUserEntityId(1L);
        comment.setUser(user); // #TODO Implements get real user
        comment.setReport(report);
        comment = commentRepository.save(comment);
        return Mapper.entityToDTO(comment);
    }

    @Transactional
    public CommentDTO updateComment(Long reportId, Long commentId, CommentDTO commentDTO) {
        Comment comment = findById(commentId);
        Report report = reportService.findReportById(reportId);
        if(!comment.getReport().getId().equals(report.getId())) {
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.COMMENT_NOT_BELONG_TO_REPORT.getMessage());
        }
        checkIfValid(report.getActive());
        checkIfSameUser(comment);
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
    // #TODO Implemnts validation for current user edit
    public void checkIfSameUser(Comment comment){
        // TODO Implements get real user
        UserEntity user = userEntityService.findByUserEntityId(1L);
        if (!comment.getUser().equals(user)){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum
                    .USER_COMMENT_DONT_MATCH.getMessage());
        }
    }

    public Comment findById (Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException(ResourceNotFoundExceptionEnum
                        .COMMENT_NOT_FOUND.build(commentId)));
    }

}
