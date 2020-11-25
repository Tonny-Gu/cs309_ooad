package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    List<TestCase> findByQuestion( Question question );
}
