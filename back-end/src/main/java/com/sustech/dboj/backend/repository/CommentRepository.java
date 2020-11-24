package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
