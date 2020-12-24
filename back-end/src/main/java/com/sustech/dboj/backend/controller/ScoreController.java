package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "成绩表")
public class ScoreController {
    private static final Logger logger = LoggerFactory.getLogger( ScoreController.class );
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

    @PostMapping("/super/score")
    @ApiOperation(value = "按条件获取成绩表(教师)")
    public List<Score> getScoreTech( Integer user_id , Integer contest_id , Integer question_id ) {
        return getScore( user_id , contest_id , question_id );
    }

    @PostMapping("/super/score/submission")
    @ApiOperation(value = "获取AC代码(教师)")
    public Map<String, List<Submission>> getScoreSubmission( Integer user_id , Integer contest_id , Integer question_id ) {
        List<Score> scoreList = getScore( user_id , contest_id , question_id );
        Map<String, List<Submission>> result = new HashMap<>( );
        for (Score score : scoreList) {
            Submission submission = submissionRepository.findById( score.getSubmissionId( ) ).orElse( null );
            String username = score.getStudent( ).getUsername( );
            if ( !result.containsKey( username ) ) {
                result.put( username , new ArrayList<>( ) );
            }
            result.get( username ).add( submission );
        }
        return result;
    }

    @PostMapping("/super/score/download")
    @ApiOperation(value = "按条件下载成绩表(教师)")
    public void getScoreDownload( HttpServletResponse response , Integer user_id , Integer contest_id , Integer question_id ) {
        List<Score> scoreList = getScore( user_id , contest_id , question_id );
        File file = new File( "score.csv" );
        response.setHeader( "content-type" , "application/octet-stream" );
        response.setContentType( "application/octet-stream" );
        response.setHeader( "Content-Disposition" , "attachment;filename=" + file );
        try {
            OutputStream out = new FileOutputStream( file );
            StringBuilder result = new StringBuilder( );
            result.append( "id,student,contest,question,submit,ac,wa,acTime\n" );
            for (Score score : scoreList) {
                result.append( score.toString( ) ).append( '\n' );
            }
            byte[] b = result.toString( ).getBytes( );
            out.write( b );
            out.close( );
        } catch (Exception e) {
            e.printStackTrace( );
        }
        byte[] buff = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream( file );
            bis = new BufferedInputStream( fis );
            OutputStream os = response.getOutputStream( );
            int i = bis.read( buff );
            while (i != -1) {
                os.write( buff , 0 , i );
                i = bis.read( buff );
            }
        } catch (IOException e) {
            e.printStackTrace( );
        } finally {
            if ( bis != null ) {
                try {
                    bis.close( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
            if ( fis != null ) {
                try {
                    fis.close( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        }
    }


    @PostMapping("/user/score")
    @ApiOperation(value = "按条件获取成绩表(学生)")
    public List<Score> getScoreUser( Integer user_id , Integer contest_id , Integer question_id ) {
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
            scores = scoreRepository.findByStudentAndQuestion( student , question );
        } else if ( contest != null ) {
            scores = scoreRepository.findByStudentAndContest( student , contest );
        } else {
            scores = scoreRepository.findByStudent( student );
        }
        return scores;
    }

    @GetMapping("/user/contest/rank")
    @ApiOperation(value = "竞赛排名(登陆可见)")
    public List<Object[]> getRank( Integer contest_id ) {
        return scoreRepository.getContestRank( contest_id );
    }

    private List<Score> getScore( Integer user_id , Integer contest_id , Integer question_id ) {
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
}
