package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
}
