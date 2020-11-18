package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {
}
