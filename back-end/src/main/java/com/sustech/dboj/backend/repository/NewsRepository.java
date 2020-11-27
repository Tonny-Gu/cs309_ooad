package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query(value = "select * from notice where en_able=true order by id desc;",nativeQuery=true)
    List<News> findCurrentNotice();

    @Transactional
    @Modifying
    @Query(value = "update notice set en_able=?2 where id=?1",nativeQuery=true)
    void activeNotice( Integer id, Boolean status );

}
