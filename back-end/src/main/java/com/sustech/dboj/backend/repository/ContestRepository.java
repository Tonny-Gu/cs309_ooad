package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Integer> {
    @Query(value = "select * from contest where contest.id in ( select contests_id from contest_users where users_id=?1)",nativeQuery=true)
    List<Contest> userGetContests( Integer id);

    @Modifying
    @Query(value = "update notice set en_able=?2 where id=?1",nativeQuery=true)
    void activeContest( Integer id, Boolean status );

    Contest findByName( String name );
}
