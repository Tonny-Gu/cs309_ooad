package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    @Query(value = "select * from submit_log where student_id=?1",nativeQuery=true)
    List<Submission> getLog( Integer student);

    @Query(value = "select * from submit_log where student_id=?1 and question_id=?2",nativeQuery=true)
    List<Submission> getLog( Integer student, Integer question);
}
