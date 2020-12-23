package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.JudgeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JudgeLogRepository extends JpaRepository<JudgeLog, Integer> {
    List<JudgeLog> findBySubmission(Integer submission);
}
