package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.JsonFormat;
import com.sustech.dboj.backend.util.MqttUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
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
        //reserve check
//        Submission submission = new Submission();
//        User student = userRepository.findById( user_id ).orElse( null );
//        if ( student == null )return "err: Invalid User";
//        Question question = questionRepository.findById( question_id ).orElse( null );
//        if ( question == null )return "err: Question Not Found";
//        Contest contest = contestRepository.findById( contest_id ).orElse( null );
//        if ( contest == null )return "err: contest Not Found";
        String status = "Submit";
        code = Base64.getEncoder( ).encodeToString( code.getBytes( ) );
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String submit_time = ft.format( new Date( ) );
        User student = userRepository.findById( user_id ).orElse( null );
        if ( student == null ) return "err: student not found";
        submissionRepository.submitToDB( code , status , language , submit_time , contest_id , question_id , user_id , "" );
        Submission submission = submissionRepository.findByStudentAndSubmitTime( student , submit_time );
        Question question = questionRepository.findById( question_id ).orElse( null );
        if ( question == null ) return "err: Question Not Found";
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
        return submission.getId( ).toString( );
    }


    @PostMapping("user/submission/byId")
    @ApiOperation(value = "用户获取某提交")
    public Submission getSubmissionById( Integer id ) {
        return submissionRepository.findById( id ).orElse( null );
    }

    @PostMapping("user/submission")
    @ApiOperation(value = "按条件获取提交(用户级别)")
    public List<Submission> getSubmission( Integer user_id ,Integer contest_id , Integer question_id , Boolean withCode ) {
        List<Submission> submissions;
        System.out.println( "debug: withCode:" + withCode );
        if ( question_id == null && contest_id == null ) {
            User user = userRepository.findById( user_id ).orElse( null );
            if(user==null)return null;
            submissions = submissionRepository.getLogByStu( user_id );
        } else if ( question_id == null ) {
            submissions = submissionRepository.getLogByContest( user_id , contest_id );
        } else if( contest_id == null){
            submissions = submissionRepository.getLogByQuestion( user_id , question_id );
        }else{
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
    public List<Submission> getSubmissionByContest(Integer contest_id , String name , Boolean withCode ) {
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
