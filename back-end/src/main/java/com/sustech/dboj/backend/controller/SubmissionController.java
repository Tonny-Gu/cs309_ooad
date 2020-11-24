package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.mqtt.MqttSender;
import com.sustech.dboj.backend.repository.*;
import jdk.internal.loader.AbstractClassLoaderValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
public class SubmissionController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private MqttSender mqttSender;

    @PostMapping("/user/submit")
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
        String info = "Submit";
        Base64.getEncoder( ).encodeToString( code.getBytes( ) );
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String submit_time = ft.format( new Date( ) );
        User student = userRepository.findById( user_id ).orElse( null );
        if ( student == null )return "err: student not found";
        submissionRepository.submitToDB( code , info , language , submit_time , contest_id , question_id , user_id );
        Submission submission = submissionRepository.findByStudentAndSubmitTime( student, submit_time );
        //push to MQTT
        mqttSender.sendToMqtt("topic/submit", submission.toString());
        return "submit success";
    }

    @PostMapping("/user/submission/rank")
    public List<Submission> getRank( Integer contest_id , Integer question_id ) {
        return submissionRepository.getSubmissionRank( contest_id , question_id );
    }

    @GetMapping("admin/submission/all")
    public List<Submission> getAllSubmission() {
        return submissionRepository.findAll( );
    }

    @GetMapping("admin/submission/range")
    public List<Submission> getAllSubmission( Integer begin , Integer length ) {
        return submissionRepository.getSubmissionLimit( begin , length );
    }


}
