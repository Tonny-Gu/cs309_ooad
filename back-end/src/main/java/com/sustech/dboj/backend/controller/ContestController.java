package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping("/admin/contest/stu")
    @ApiOperation(value = "获取竞赛的参与者")
    public List<User> getUserByContest( Integer id ) {
        Contest contest = contestRepository.findById( id ).orElse( null );
        if ( contest == null ) return null;
        return userRepository.contestGetUsers( id );
    }

    @GetMapping("admin/contest")
    public List<Contest> getAdminAllContests() {
        return contestRepository.findAll( );
    }

    @GetMapping("user/contest")
    public List<Contest> getUserAllContests() {
        return contestRepository.findByEnable( true );
    }

    @GetMapping("/contest/id")
    public Contest getContestsById( Integer id ) {
        return contestRepository.findById( id ).orElse( null );
    }

    @PostMapping("/admin/contest/create")
    @ApiOperation(value = "创建竞赛")
    public Contest createContest( String name , String beginTime , String endTime ) {
        if(contestRepository.findByName( name )!=null)return null;
        Contest contest = new Contest( );
        contest.setBeginTime( beginTime );
        contest.setEndTime( endTime );
        contest.setName( name );
        contest.setEnable( checkEnable( contest ) );
        contestRepository.save( contest );
        return contest;
    }


    @PostMapping("/admin/contest/addquestion")
    @ApiOperation(value = "添加题目")
    public String addQuestion( Integer contest_id , Integer question_id ) {
        // add contest-user
        Question question = questionRepository.findById( question_id ).orElse( null );
        Contest myContest = contestRepository.findById( contest_id ).orElse( null );
        if ( question == null ) return String.format( "question:%s not found" , question_id );
        if ( myContest == null ) return String.format( "contest:%s not found" , contest_id );
        questionRepository.addQuestion( contest_id , question_id );
        for (User myUser : userRepository.contestGetUsers( contest_id )){
            Score score = new Score( );
            score.setContest( myContest );
            score.setQuestion( question );
            score.setStudent( myUser );
            scoreRepository.save( score );
        }
        logger.info( "Question: {} was added into Contest: {}" , question.getName( ) , myContest.getName( ) );
        return String.format( "success: add relation->contest %d, question: %d" , contest_id , question_id );
    }

    @PostMapping("/admin/contest/modify")
    @ApiOperation(value = "修改竞赛")
    public String modifyContest( Integer contest_id , String name , String beginTime , String endTime ) {
        Contest contest = contestRepository.findById( contest_id ).orElse( null );
        if ( contest == null ) return "error: Contest Not Found";
        if ( name != null ) contest.setName( name );
        if ( beginTime != null ) contest.setBeginTime( beginTime );
        if ( endTime != null ) contest.setEndTime( endTime );
        contest.setEnable( checkEnable( contest ) );
        contestRepository.save( contest );
        return "success: " + contest_id;
    }

    @PostMapping("/admin/contest/delquestion")
    @ApiOperation(value = "删除题目")
    public String delQuestion( Integer contest_id , Integer question_id ) {
        questionRepository.delQuestion( contest_id , question_id );
        return String.format( "success: delete relation->contest %d, question: %d" , contest_id , question_id );
    }

    private boolean checkEnable(Contest contest){
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String now = ft.format( new Date( ) );
        return contest.getBeginTime( ).compareTo( now ) < 0 && contest.getEndTime( ).compareTo( now ) > 0;
    }

}
