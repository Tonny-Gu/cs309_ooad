package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "成绩表")
public class ScoreController {
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;
    private final ContestRepository contestRepository;
    private final TestCaseRepository testCaseRepository;
    private final ScoreRepository scoreRepository;

    public ScoreController( UserRepository userRepository , SubmissionRepository submissionRepository , QuestionRepository questionRepository , ContestRepository contestRepository , TestCaseRepository testCaseRepository , ScoreRepository scoreRepository ) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
        this.contestRepository = contestRepository;
        this.testCaseRepository = testCaseRepository;
        this.scoreRepository = scoreRepository;
    }

    @GetMapping("super/score")
    @ApiOperation(value = "按条件获取成绩表(教师)")
    public List<Score> getScore( Integer user_id , Integer contest_id , Integer question_id ) {
        User student = null;
        Contest contest = null;
        Question question = null;
        List<Score> scores = new ArrayList<>( );
        if ( user_id != null ) student = userRepository.findById( user_id ).orElse( null );
        if ( question_id != null ) question = questionRepository.findById( question_id ).orElse( null );
        if ( contest_id != null ) contest = contestRepository.findById( contest_id ).orElse( null );
        if ( student != null && question != null && contest != null ) {
            scores.add( scoreRepository.findByStudentAndQuestionAndContest( student , question , contest ) );

        } else if ( student != null && question != null ) {
            scores = scoreRepository.findByStudentAndQuestion( student , question );
        } else if ( student != null && contest != null ) {
            scores = scoreRepository.findByStudentAndContest( student , contest );
        } else if ( question != null && contest != null ) {
            scores = scoreRepository.findByContestAndQuestion( contest , question );
        } else if ( student != null ) {
            scores = scoreRepository.findByStudent( student );
        } else if ( question != null ) {
            scores = scoreRepository.findByQuestion( question );
        } else if ( contest != null ) {
            scores = scoreRepository.findByContest( contest );
        } else {
            scores = scoreRepository.findAll( );
        }
        return scores;
    }

    @GetMapping("user/score")
    @ApiOperation(value = "按条件获取成绩表(学生)")
    public List<Score> getScoreUser( Integer user_id ,Integer contest_id , Integer question_id ) {
        User student = userRepository.findById( user_id ).orElse( null );
        assert student != null;
        Contest contest = null;
        Question question = null;
        List<Score> scores = new ArrayList<>( );
        if ( question_id != null ) question = questionRepository.findById( question_id ).orElse( null );
        if ( contest_id != null ) contest = contestRepository.findById( contest_id ).orElse( null );
        if ( question != null && contest != null ) {
            scores.add( scoreRepository.findByStudentAndQuestionAndContest( student , question , contest ) );
        } else if ( question != null ) {
            scores = scoreRepository.findByStudentAndQuestion( student, question );
        } else if ( contest != null ) {
            scores = scoreRepository.findByStudentAndContest( student, contest );
        }else{
            scores = scoreRepository.findByStudent( student );
        }
        return scores;
    }

    @GetMapping("user/contest/rank")
    @ApiOperation(value = "竞赛排名(登陆可见)")
    public List<Object[]> getRank(Integer contest_id){
        return scoreRepository.getContestRank( contest_id );
    }
}
