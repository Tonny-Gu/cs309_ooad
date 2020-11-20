package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "select * from contest_users where users_id=?1",nativeQuery=true)
    Set<Contest> getContests( Integer id);


}
