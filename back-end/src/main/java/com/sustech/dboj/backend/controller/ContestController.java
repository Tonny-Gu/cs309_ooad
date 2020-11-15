package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

public class ContestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private ContestRepository contestRepository;


}
