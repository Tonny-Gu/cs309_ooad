package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {

    Score findByStudentAndQuestionAndContest( User student , Question question , Contest contest );

    List<Score> findByStudent( User student );

    List<Score> findByContest( Contest contest );

    List<Score> findByQuestion( Question question );

    List<Score> findByStudentAndContest( User student , Contest contest );

    List<Score> findByStudentAndQuestion( User student , Question question );

    List<Score> findByContestAndQuestion( Contest contest , Question question );

    @Query(value = "select student_id, sum(ac::int) as ac_num, max(ac_time) as last_ac_time from score where contest_id = ?1 group by student_id order by ac_num, last_ac_time desc;", nativeQuery = true)
    List<Object[]> getContestRank(Integer contest);
}
