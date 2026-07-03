package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentDTO;
import com.control_activos.sks.control_activos.models.dto.commentDTO.CommentRequestDTO;
import com.control_activos.sks.control_activos.models.entity.Comment;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.CommentRepository;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserEntityService userEntityService;

    private final CommentRepository commentRepository;
    private final FormatDataValidationService formatDataValidationService;
    private final ReportService reportService;


    // HERE BEGINS THE COMMENT SERVICE METHODS VERIFIED WITH ANGULAR FRONTEND.
    @Transactional
    public CommentDTO saveComment(Long reportId, CommentRequestDTO commentRequestDTO) {
        Report report = reportService.findReportById(reportId);
        checkIfReportIsActive(report.getStatus());
        report.setUpdatedAt(OffsetDateTime.now());
        Comment comment = new Comment();
        comment.setText(formatDataValidationService.lowerCase(commentRequestDTO.getText()));
        comment.setCreatedAt(OffsetDateTime.now());

        // User Implementation Testing
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userEntityService.findByUserEntityByUsername(username);
        comment.setUser(user);

        comment.setReport(report);
        comment = commentRepository.save(comment);
        return Mapper.entityToDTO(comment);
    }

    public void checkIfReportIsActive(Boolean active) {
        if (!active) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.REPORT_ALREADY_CLOSED.getMessage()
            );
        }
    }

    // HERE BEGINS THE COMMENT SERVICE OF METHODS NOT VERIFIED WITH ANGULAR FRONTEND, METHODS BELOW THIS COMMENT ARE NOT VERIFIED WITH ANGULAR FRONTEND.

    @Transactional
    public CommentDTO updateComment(Long reportId, Long commentId, CommentDTO commentDTO) {
        Comment comment = findById(commentId);
        Report report = reportService.findReportById(reportId);
        if(!comment.getReport().getId().equals(report.getId())) {
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.COMMENT_NOT_BELONG_TO_REPORT.getMessage());
        }
        checkIfReportIsActive(report.getStatus());
        checkIfSameUser(comment);
        comment.setText(formatDataValidationService.lowerCase(commentDTO.getText()));
        comment = commentRepository.save(comment);
        return Mapper.entityToDTO(comment);
    }


    // #TODO Implemnts validation for current user edit
    public void checkIfSameUser(Comment comment){
        // TODO Implements get real user
        UserEntity user = userEntityService.findByUserEntityById(1L);
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
