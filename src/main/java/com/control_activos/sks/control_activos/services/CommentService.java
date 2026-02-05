package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.exception.NotFoundResourceException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.CommentDTO;
import com.control_activos.sks.control_activos.models.entity.Comment;
import com.control_activos.sks.control_activos.models.entity.Report;
import com.control_activos.sks.control_activos.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReportService reportService;
    public  CommentService(CommentRepository commentRepository, ReportService reportService) {
        this.commentRepository = commentRepository;
        this.reportService = reportService;
    }

    public CommentDTO saveComment(Long reportId, CommentDTO commentDTO) {
        Report report = reportService.findReportById(reportId);
        if(report.getActive().equals(false)){
            // #TODO Implements Error enum exception
            throw new NotFoundResourceException("Report not active");
        }
        String commentText = commentDTO.getText().trim().toLowerCase();
        Comment comment = new Comment();
        comment.setText(commentText);
        comment = commentRepository.save(comment);
        return Mapper.entityToDTO(comment);
    }

}
