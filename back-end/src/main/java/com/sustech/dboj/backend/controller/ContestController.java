package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private ContestRepository contestRepository;

    @GetMapping("/admin/contest/stu")
    public List<User> getUserByContest( Integer id ) {
        Contest contest = contestRepository.findById( id ).orElse( null );
        if ( contest == null ) return null;
        return userRepository.contestGetUsers( id );
    }

    @GetMapping("/contest")
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @GetMapping("/contest")
    public Contest getContestsById(Integer id) {
        return contestRepository.findById( id ).orElse( null );
    }

}
