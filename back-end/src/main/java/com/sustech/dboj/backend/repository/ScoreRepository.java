package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {

    Score findByStudentAndQuestionAndContest( User student , Question question , Contest contest );

    List<Score> findByStudent( User student );

    List<Score> findByContest( Contest contest );

    List<Score> findByQuestion( Question question );

    List<Score> findByStudentAndContest( User student , Contest contest );

    List<Score> findByStudentAndQuestion( User student , Question question );

    List<Score> findByContestAndQuestion( Contest contest , Question question );

    @Query(value = "select ROW_NUMBER()OVER() as rank, user_info.username, user_info.nickname, ac_num, last_ac_time\n" +
            "from\n" +
            "(select student_id, sum(ac\\:\\:int) as ac_num, max(ac_time) as last_ac_time\n" +
            "from score\n" +
            "where contest_id = ?1\n" +
            "group by student_id\n" +
            "order by ac_num desc ,last_ac_time) a\n" +
            "join user_info\n" +
            "on a.student_id = user_info.id", nativeQuery = true)
    List<Object[]> getContestRank(Integer contest);

    @Transactional
    @Modifying
    @Query(value = "update score set contest_id=?2, question_id=?3, student_id=?4 where id=?1",nativeQuery=true)
    void updateContestAndQuestion( Integer id, Integer contest, Integer question, Integer student );
}
