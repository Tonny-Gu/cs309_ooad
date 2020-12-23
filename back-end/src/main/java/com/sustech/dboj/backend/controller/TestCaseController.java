package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.TestCase;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.TestCaseRepository;
import com.sustech.dboj.backend.util.FileUtil;
import com.sustech.dboj.backend.util.JsonFormat;
import com.sustech.dboj.backend.util.MqttUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@Api(tags = "测试用例管理")
public class TestCaseController {
    private static final Logger logger = LoggerFactory.getLogger( TestCaseController.class );
    private static final String envPathName = "env/";//path of testcase environment '.sql'
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @PostMapping("/admin/testcase/upload")
    @ApiOperation( value = "上传测试用例")
    public String uploadTestcase( MultipartFile initFile , Integer questionId ) {
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
        testCaseRepository.save( testCase );
        String fileName =  UUID.randomUUID().toString();
        try {
            FileUtil.fileStore( initFile, envPathName + fileName + ".sql" );
            testCase.setInitDB( new String(initFile.getBytes( ),  StandardCharsets.UTF_8) );
        } catch (IOException e) {
            e.printStackTrace( );
            return "error: " + e.getMessage( );
        }

        //push to MQTT
        String broker = "tcp://192.168.122.10:1883" ;
        String topic =  "env/send";
        int qos  = 2;
        try {
            String messageJson = JsonFormat.initFormat( testCase, question.getDbType() );
            MqttUtil.sender( broker,topic,qos, messageJson );
        } catch (JsonProcessingException | MqttException e) {
            e.printStackTrace( );
        }
        testCase.setInitDB( envPathName + fileName + ".sql" );
        testCaseRepository.save( testCase );
        testCaseRepository.updateQuestion( testCase.getId() , questionId );
        return "success: " + testCase.getId();
     }
}
