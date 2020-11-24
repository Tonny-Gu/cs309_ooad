package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.TextChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Configuration
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping("/register")
    public String register( String username , String password , String name , String role ) {
        if ( !TextChecker.userNameChecker( username ) ) return "Invalid username";
        if ( userRepository.findByUsername( username ) != null ) return "Username have been used";
        if ( ( !role.contains( "TA" ) ) && ( !role.contains( "SA" ) ) && ( !role.contains( "STU" ) ) )
            return "Invalid role";
        if ( !TextChecker.passwordChecker( password ) ) return "Invalid password";
        User newUser = new User( );
        newUser.setName( name );
        newUser.setUsername( username );
        String[] authorities = role.split( "," );
        for (String e : authorities) {
            newUser.setRole( "ROLE_" + role );//坑：自己写的role要在前面加上ROLE_ 因为库里面自带的比较会在角色前面加上这个
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( );
        newUser.setPassword( encoder.encode( password ) );
        newUser.setNickname( name );
        userRepository.save( newUser );
        log.info( "User: {}, name: {} have been created",newUser.getUsername(), newUser.getName() );
        return "Create user successful";
    }

    @GetMapping("/user/get/contests")
    public List<Contest> getContests( Integer id ) {
        User user = userRepository.findById( id ).orElse( null );
        if ( user == null ) return null;
        return contestRepository.userGetContests( id );
    }

    @GetMapping("/user/get/submission")
    public List<Submission> getSubmissionsByUser( Integer id ) {
        User user = userRepository.findById( id ).orElse( null );
        if ( user == null ) return null;
        return submissionRepository.getLog( id );
    }

    @PostMapping("/user/get/submission")
    public List<Submission> getSubmissions( Integer id , Integer question_id ) {
        User user = userRepository.findById( id ).orElse( null );
        if ( user == null ) return null;
        Question question = questionRepository.findById( id ).orElse( null );
        if ( question == null ) return null;
        return submissionRepository.getLog( id , question_id );
    }

    @Transactional
    @PostMapping("/user/joinContest")
    public String joinContest( Integer user_id , Integer contest_id ) {
        // add contest-user
        User myUser = userRepository.findById( user_id ).orElse( null );
        Contest myContest = contestRepository.findById( contest_id ).orElse( null );
        if ( myUser == null ) return "User not found";
        if ( myContest == null ) return "Contest not found";
        int statusCode = userRepository.joinContest( user_id , contest_id );
        System.out.println( "statusCode=" + statusCode );
        log.info( "User: {} join Contest: {}",myUser.getUsername(),myContest.getName() );
        // create score table
        for (Question question : myContest.getQuestions()){
            Score score = new Score();
            score.setContest( myContest );
            score.setQuestion( question );
            score.setStudent( myUser );
            scoreRepository.save( score );
        }
        return "Join contest successfully";
    }


    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
