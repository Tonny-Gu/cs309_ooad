package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByName(String name);

    List<Question> findByAuthor( User author );

    List<Question> findByDegree( String degree );

    List<Question> findByDbType( String dbType );

    @Transactional
    @Modifying
    @Query(value = "insert into contest_questions (contests_id, questions_id) values (?1,?2)",nativeQuery=true)
    void addQuestion( Integer contest_id, Integer question_id);

    @Transactional
    @Modifying
    @Query(value = "delete from contest_questions where contests_id=?1 and questions_id=?2",nativeQuery=true)
    void delQuestion( Integer contest_id, Integer question_id);

    @Query(value = "select * from question where question.id in (select questions_id from contest_questions where contests_id=?1)",nativeQuery=true)
    List<Question> contestGetQuestions( Integer id);

    @Query(value = "select count(*) from question",nativeQuery=true)
    List<Object[]> getQuestionsNum();

}
