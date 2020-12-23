package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "竞赛管理")
public class ContestController {
    private static final Logger logger = LoggerFactory.getLogger( UserController.class );
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
        return contestRepository.findAll( );
    }

    @GetMapping("/contest/id")
    public Contest getContestsById( Integer id ) {
        return contestRepository.findById( id ).orElse( null );
    }

    @PostMapping("/admin/contest/create")
    public Contest createContest( String name , String beginTime , String endTime ) {
        Contest contest = new Contest( );
        contest.setBeginTime( beginTime );
        contest.setEndTime( endTime );
        contest.setName( name );
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String now = ft.format( new Date( ) );
        if ( contest.getBeginTime( ).compareTo( now ) < 0 && contest.getEndTime( ).compareTo( now ) > 0 ) {
            contest.setEnable( true );
            logger.info( "Contest:{} is enable" , contest.getName( ) );
        } else {
            contest.setEnable( false );
            logger.info( "Contest:{} is disable" , contest.getName( ) );
        }
        contestRepository.save( contest );
        return contest;
    }


    @PostMapping("/admin/contest/addquestion")
    public String addQuestion( Integer contest_id , Integer question_id ) {
        // add contest-user
        Question question = questionRepository.findById( question_id ).orElse( null );
        Contest myContest = contestRepository.findById( contest_id ).orElse( null );
        if ( question == null ) return String.format( "question:%s not found" , question_id );
        if ( myContest == null ) return String.format( "contest:%s not found" , contest_id );
        questionRepository.addQuestion( contest_id , question_id );
        logger.info( "Question: {} was added into Contest: {}" , question.getName( ) , myContest.getName( ) );
        return String.format( "success: add relation->contest %d, question: %d" , contest_id , question_id );
    }

    @PostMapping("/admin/contest/modify")
    public String modifyContest( Integer contest_id , String name , String beginTime , String endTime ) {
        Contest contest = contestRepository.findById( contest_id ).orElse( null );
        if ( contest == null ) return "error: Contest Not Found";
        if ( name != null ) contest.setName( name );
        if ( beginTime != null ) contest.setBeginTime( beginTime );
        if ( endTime != null ) contest.setEndTime( endTime );
        contestRepository.save( contest );
        return "success: " + contest_id;
    }

    @PostMapping("/admin/contest/delquestion")
    public String delQuestion( Integer contest_id , Integer question_id ) {
        questionRepository.delQuestion( contest_id , question_id );
        return String.format( "success: delete relation->contest %d, question: %d" , contest_id , question_id );
    }

}
