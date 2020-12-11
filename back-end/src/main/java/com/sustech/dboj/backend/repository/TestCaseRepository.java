package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    List<TestCase> findByQuestion( Question question );

    @Transactional
    @Modifying
    @Query(value = "update test_case set env=?2 where id=?1",nativeQuery=true)
    void initEnv( Integer id, String env );
}
