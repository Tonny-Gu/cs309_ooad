package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.JsonFormat;
import com.sustech.dboj.backend.util.MqttUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "交题管理")
public class SubmissionController {
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;
    private final ContestRepository contestRepository;
    private final TestCaseRepository testCaseRepository;

    public SubmissionController( UserRepository userRepository , SubmissionRepository submissionRepository , QuestionRepository questionRepository , ContestRepository contestRepository , TestCaseRepository testCaseRepository ) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
        this.contestRepository = contestRepository;
        this.testCaseRepository = testCaseRepository;
    }


    @PostMapping("/user/submit")
    @ApiOperation(value = "交题接口")
    public String submitCode( Integer user_id , Integer question_id , Integer contest_id , String code , String language ) {
        //put into DB
        if(language.equalsIgnoreCase( "mysql" ))language = "MySQL";
        else if(language.equalsIgnoreCase( "postgresql" ))language = "PostgreSQL";
        else if(language.equalsIgnoreCase( "sqlite" ))language = "SQLite";
        else return "err: Language Error";
        String status = "Submit";
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String submit_time = ft.format( new Date( ) );
        User student = userRepository.findById( user_id ).orElse( null );
        if ( student == null ) return "err: student not found";
        Question question = questionRepository.findById( question_id ).orElse( null );
        if ( question == null ) return "err: Question Not Found";
        Contest contest = contestRepository.findById( contest_id ).orElse( null );
        if ( contest == null ) return "err: Contest Not Found";

        if(student.getRole().equals( "ROLE_STU" ) && !contest.getEnable())
            return "err: Contest Not Start";
        Submission submission = new Submission();
        submission.setCode( code );
        submission.setStatus( status );
        submission.setSubmitTime( submit_time );
        submission.setLanguage( language );
        submission.setInfo( "" );
        submissionRepository.save( submission );
        code = Base64.getEncoder( ).encodeToString( code.getBytes( ) );

        //push to MQTT

        List<TestCase> testCases = testCaseRepository.findByQuestion( question );
        String broker = "tcp://192.168.122.10:1883";
        String topic = "code/send";
        int qos = 2;
        try {
            String messageJson = JsonFormat.submitFormat( submission , question , testCases );
            MqttUtil.sender( broker , topic , qos , messageJson );
        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace( );
        }
        submissionRepository.updateContestAndUserAndQuestion( submission.getId(), contest_id, user_id,question_id );
        submissionRepository.updateCode( submission.getId(), code );
        return submission.getId( ).toString( );
    }


    @PostMapping("user/submission/byId")
    @ApiOperation(value = "用户获取某提交")
    public Submission getSubmissionById( Integer id ) {
        return submissionRepository.findById( id ).orElse( null );
    }

    @PostMapping("user/submission")
    @ApiOperation(value = "按条件获取提交(用户级别)")
    public List<Submission> userGetSubmission( Integer user_id , Integer contest_id , Integer question_id , Boolean withCode ) {
        List<Submission> submissions;
        if ( question_id == null && contest_id == null ) {
            User user = userRepository.findById( user_id ).orElse( null );
            if ( user == null ) return null;
            submissions = submissionRepository.getLogByStu( user_id );
        } else if ( question_id == null ) {
            submissions = submissionRepository.getLogByContest( user_id , contest_id );
        } else if ( contest_id == null ) {
            submissions = submissionRepository.getLogByQuestion( user_id , question_id );
        } else {
            submissions = submissionRepository.getLog( user_id , question_id , contest_id );
        }
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("/admin/submission/rank")
    @ApiOperation(value = "获取某赛题成功提交排名")
    public List<Submission> getRank( Integer contest_id , Integer question_id , Boolean withCode ) {
        List<Submission> submissions = submissionRepository.getSubmissionRank( contest_id , question_id );
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("/admin/submission/contest")
    @ApiOperation(value = "获取某竞赛所有提交")
    public List<Submission> getSubmissionByContest( Integer contest_id , String name , Boolean withCode ) {
        if ( contest_id == null && name == null ) return null;
        List<Submission> submissions;
        if ( contest_id != null ) {
            submissions = submissionRepository.getLogByContest( contest_id );
        } else {
            Contest contest = contestRepository.findByName( name );
            assert contest != null;
            submissions = submissionRepository.findByContest( contest );
        }
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("/admin/submission/question")
    @ApiOperation(value = "获取某题所有提交")
    public List<Submission> getSubmissionByQuestion( Integer question_id , String name , Boolean withCode ) {
        if ( question_id == null && name == null ) return null;
        List<Submission> submissions;
        if ( question_id != null ) {
            submissions = submissionRepository.getLogByQuestion( question_id );
        } else {
            Question question = questionRepository.findByName( name );
            assert question != null;
            submissions = submissionRepository.findByQuestion( question );
        }
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("admin/submission/all")
    @ApiOperation(value = "按条件获取提交(管理员级别)")
    public List<Submission> adminGetSubmission( Integer user_id , Integer contest_id , Integer question_id , Boolean withCode ) {
        List<Submission> submissions;
        if ( user_id == null && question_id == null && contest_id == null ) {
            submissions = submissionRepository.findAll( );
        } else if ( question_id == null && contest_id == null ) {
            submissions = submissionRepository.getLogByStu( user_id );
        } else if ( user_id == null && contest_id == null ) {
            submissions = submissionRepository.getLogByQuestion( question_id );
        } else if ( user_id == null && question_id == null ) {
            submissions = submissionRepository.getLogByContest( contest_id );
        } else if ( question_id == null ) {
            submissions = submissionRepository.getLogByContest( user_id , contest_id );
        } else if ( contest_id == null ) {
            submissions = submissionRepository.getLogByQuestion( user_id , question_id );
        } else if ( user_id == null ) {
            submissions = submissionRepository.getLogByQuestionAndContest( question_id , contest_id );
        } else {
            submissions = submissionRepository.getLog( user_id , question_id , contest_id );
        }
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("admin/submission")
    @ApiOperation(value = "获取所有提交")
    public List<Submission> getAllSubmission( Boolean withCode ) {
        List<Submission> submissions = submissionRepository.findAll( );
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }

    @PostMapping("admin/submission/all/range")
    @ApiOperation(value = "获取某个范围提交")
    public List<Submission> getSomeSubmission( Integer begin , Integer length , Boolean withCode ) {
        List<Submission> submissions = submissionRepository.getSubmissionLimit( begin , length );
        if ( !withCode ) {
            for (Submission submission : submissions) {
                submission.setCode( null );
            }
        }
        return submissions;
    }
}
