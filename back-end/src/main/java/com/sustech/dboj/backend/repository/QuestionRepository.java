package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByName(String name);

}
