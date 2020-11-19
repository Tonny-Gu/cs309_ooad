package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.News;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Submission;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query(value = "select * from notice where en_able=true order by id desc;",nativeQuery=true)
    List<News> findCurrentNotice();

    @Query(value = "update notice set en_able=?2 where id=?1",nativeQuery=true)
    void activeNotice( Integer id, Boolean status );

}
