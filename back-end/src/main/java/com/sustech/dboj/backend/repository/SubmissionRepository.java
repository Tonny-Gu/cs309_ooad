package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    @Query(value = "select * from submit_log where student_id=?1", nativeQuery = true)
    List<Submission> getLogByStu( Integer student );

    @Query(value = "select * from submit_log where question_id=?1", nativeQuery = true)
    List<Submission> getLogByQuestion( Integer question );

    @Query(value = "select * from submit_log where contest_id=?1", nativeQuery = true)
    List<Submission> getLogByContest( Integer contest );

    @Query(value = "select * from submit_log where student_id=?1 and question_id=?2", nativeQuery = true)
    List<Submission> getLogByQuestion( Integer student , Integer question );

    @Query(value = "select * from submit_log where student_id=?1 and contest_id=?2", nativeQuery = true)
    List<Submission> getLogByContest( Integer student , Integer contest );

    @Query(value = "select * from submit_log where question_id=?1 and contest_id=?2", nativeQuery = true)
    List<Submission> getLogByQuestionAndContest( Integer question , Integer contest );

    @Query(value = "select * from submit_log where student_id=?1 and question_id=?2 and contest_id=?3 order by id desc ", nativeQuery = true)
    List<Submission> getLog( Integer student , Integer question, Integer contest );

    List<Submission> findByContest( Contest contest );

    List<Submission> findByQuestion( Question question );

    @Transactional
    @Modifying
    @Query(value = "insert into submit_log (code, status, language, submit_time, contest_id, question_id, student_id, info) values (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void submitToDB( String code , String status , String language , String submitTime , Integer contest , Integer question , Integer student ,String info);

    @Query(value = "select * from submit_log where contest_id=?1 and question_id=?2 and info='AC' order by id ", nativeQuery = true)
    List<Submission> getSubmissionRank( Integer contest , Integer question );

    @Query(value = "select * from submit_log limit ?2 offset ?1", nativeQuery = true)
    List<Submission> getSubmissionLimit( Integer begin , Integer length );


    Submission findByStudentAndSubmitTime( User student , String submitTime );

    @Transactional
    @Modifying
    @Query(value = "update submit_log set info=?2, status=?3 where id=?1",nativeQuery=true)
    void updateInfo( Integer id, String info, String status );

    @Query(value = "select count(distinct question_id) from submit_log where student_id=?1", nativeQuery = true)
    List<Object[]> doneQuestion( Integer user_id );

    @Query(value = "select substr(submit_time,0,11), status, count(substr(submit_time,0,11)) from submit_log where submit_time between ?1 and ?2 group by substr(submit_time,0,11),status order by substr(submit_time,0,11)", nativeQuery = true)
    List<Object[]> dataBetweenTime7d (String beginTime, String endTime );

    @Query(value = "select substr(submit_time,0,14), status, count(substr(submit_time,0,14)) from submit_log where submit_time between ?1 and ?2 group by substr(submit_time,0,14),status order by substr(submit_time,0,14)", nativeQuery = true)
    List<Object[]> dataBetweenTime24h( String begin, String end );
}
