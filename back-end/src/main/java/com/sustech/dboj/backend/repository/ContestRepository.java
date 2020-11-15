package com.sustech.dboj.backend.repository;

import com.sustech.dboj.backend.domain.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Integer> {
}
