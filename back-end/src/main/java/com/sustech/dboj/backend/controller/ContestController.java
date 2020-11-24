package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ContestController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/admin/contest/stu")
    public List<User> getUserByContest( Integer id ) {
        Contest contest = contestRepository.findById( id ).orElse( null );
        if ( contest == null ) return null;
        return userRepository.contestGetUsers( id );
    }

    @GetMapping("/contest")
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @GetMapping("/contest/id")
    public Contest getContestsById(Integer id) {
        return contestRepository.findById( id ).orElse( null );
    }

    @PostMapping("/admin/contest/create")
    public Contest createContest(String name, String beginTime, String endTime){
        Contest contest = new Contest();
        contest.setBeginTime( beginTime );
        contest.setEndTime( endTime );
        contest.setName( name );
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        String now = ft.format( new Date( ) );
        if(contest.getBeginTime( ).compareTo( now ) < 0){
            contest.setEnable( true );
        }
        contestRepository.save( contest );
        return contest;
    }
    @PostMapping("/admin/contest/addquestion")
    public String addQuestion( Integer contest_id, Integer question_id){
        // add contest-user
        Question question = questionRepository.findById( question_id ).orElse( null );
        Contest myContest = contestRepository.findById( contest_id ).orElse( null );
        if ( question == null ) return String.format( "question:%s not found" , question_id);
        if ( myContest == null ) return String.format( "contest:%s not found" , contest_id);
        int statusCode = questionRepository.addQuestion( contest_id , question_id );
        System.out.println( "statusCode=" + statusCode );
        log.info( "Question: {} was added into Contest: {}",question.getName(),myContest.getName() );
        return "Join contest successfully";
    }

    @PostMapping("/admin/contest/modify")
    public Contest modifyContest(Contest contest) {
        contestRepository.save( contest );
        return contestRepository.findById( contest.getId() ).orElse( null );
    }

}
