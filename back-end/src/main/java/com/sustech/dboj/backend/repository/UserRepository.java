package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = "select * from user_info where user_info.id in (select users_id from contest_users where contests_id=?1)",nativeQuery=true)
    List<User> contestGetUsers( Integer id);


    @Query(value = "insert into contest_users (contests_id, users_id) values (?1,?2)",nativeQuery=true)
    int joinContest( Integer user_id, Integer contest_id);


}
