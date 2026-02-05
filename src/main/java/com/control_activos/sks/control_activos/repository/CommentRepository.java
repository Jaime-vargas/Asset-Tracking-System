package com.control_activos.sks.control_activos.repository;

import com.control_activos.sks.control_activos.models.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
