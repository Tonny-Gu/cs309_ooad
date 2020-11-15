package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

public class SubmissionController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    @PostMapping("/user/submit")
    public String submitCode(Integer user_id, String code, String language) {


        return null;
    }

    @PostMapping("/user/getLog/question")
    public List<Submission> getLog( Integer user_id, Integer question_id) {
        return submissionRepository.getLog( user_id, question_id );
    }

    @PostMapping("/user/getLog/all")
    public List<Submission> getLog(Integer user_id) {
        return submissionRepository.getLog( user_id );
    }
}
