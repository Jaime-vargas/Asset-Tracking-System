package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.CommentDTO;
import com.control_activos.sks.control_activos.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports/{reportId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> saveComment(@PathVariable Long reportId, @RequestBody CommentDTO commentDTO) {
        commentDTO = commentService.saveComment(reportId, commentDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable long reportId, @PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        commentDTO = commentService.updateComment(reportId, commentId, commentDTO);
        return   ResponseEntity.status(HttpStatus.OK).body(commentDTO);
    }
}
