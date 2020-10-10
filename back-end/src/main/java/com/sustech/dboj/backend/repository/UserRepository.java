package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
