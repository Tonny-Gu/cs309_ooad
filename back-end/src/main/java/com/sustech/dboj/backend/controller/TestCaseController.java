package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.TestCase;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.TestCaseRepository;
import com.sustech.dboj.backend.util.IOUtil;
import com.sustech.dboj.backend.util.JsonFormat;
import com.sustech.dboj.backend.util.MqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
public class TestCaseController {
    private static final String envPathName = "env/";//path of testcase environment '.sql'
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/admin/testcase/upload")
    public String uploadTestcase( MultipartFile initFile , Integer questionId ) throws JsonProcessingException {
        if ( initFile.isEmpty( ) ) {
            return "error: init file is empty";
        }
        Question question = questionRepository.findById( questionId ).orElse( null );
        if ( question == null ) return "error: question not found";
//        if ( initFile.getContentType( ) != null && !initFile.getContentType( ).equals( "text/sql" ) && !initFile.getContentType( ).equals( "text/x-sql" ) ) {
//            return "error: initFile not sql file";
//        }
//        if ( ansFile.getContentType( ) != null && !ansFile.getContentType( ).equals( "text/sql" ) && !ansFile.getContentType( ).equals( "text/x-sql" ) ) {
//            return "error: ansFile not sql file";
//        }
        TestCase testCase = new TestCase( );
        try {
            IOUtil.fileStore( initFile, envPathName + questionId + ".sql" );
            testCase.setInitDB( Base64.getEncoder( ).encodeToString(initFile.getBytes()) );
        } catch (IOException e) {
            e.printStackTrace( );
            return "error: " + e.getMessage( );
        }
        testCase.setQuestion( question );
        //push to MQTT
        String broker = "tcp://192.168.122.10:1883" ;
        String topic =  "init/to_judge";
        int qos  = 2;
        try {
            String messageJson = JsonFormat.initFormat( testCase );
            MqttUtil.sender( broker,topic,qos, messageJson );
        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace( );
        }
        testCase.setInitDB( envPathName + questionId + ".sql" );
        testCaseRepository.save( testCase );
        return "success: " + testCase.getId();
     }
}
