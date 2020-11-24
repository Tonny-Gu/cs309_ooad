package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    @Query(value = "select * from submit_log where student_id=?1", nativeQuery = true)
    List<Submission> getLog( Integer student );

    @Query(value = "select * from submit_log where student_id=?1 and question_id=?2", nativeQuery = true)
    List<Submission> getLog( Integer student , Integer question );

    @Query(value = "insert into submit_log (code, info, language, submit_time, contest_id, question_id, student_id) values (?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    void submitToDB( String code , String info , String language , String submitTime , Integer contest , Integer question , Integer student );

    @Query(value = "select * from submit_log where contest_id=?1 and question_id=?2 and info='AC' order by id ", nativeQuery = true)
    List<Submission> getSubmissionRank( Integer contest , Integer question );

    @Query(value = "select * from submit_log limit ?2 offset ?1", nativeQuery = true)
    List<Submission> getSubmissionLimit( Integer begin , Integer length );

    Submission findByStudentAndSubmitTime( User student , String submitTime );
}
