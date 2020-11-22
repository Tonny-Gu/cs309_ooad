package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByName(String name);

    List<Question> findByAuthor( User author );

    List<Question> findByDegree( String degree );

    List<Question> findByDbType( String dbType );


    @Query(value = "insert into contest_questions (contests_id, questions_id) values (?1,?2)",nativeQuery=true)
    int addQuestion( Integer contest_id, Integer question_id);

    @Query(value = "select * from question where question.id in (select questions_id from contest_questions where contests_id=?1)",nativeQuery=true)
    List<Question> contestGetQuestions( Integer id);
}
