package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.JudgeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeLogRepository extends JpaRepository<JudgeLog, Integer> {


}
