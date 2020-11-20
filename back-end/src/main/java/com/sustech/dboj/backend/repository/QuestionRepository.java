package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByName(String name);

    @Query(value = "select * from question where question.id in (select questions_id from contest_questions where contests_id=?1)",nativeQuery=true)
    List<Question> contestGetQuestions( Integer id);
}
