package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.TestCase;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.util.IOUtil;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
public class TestCaseController {
    private static final String envPathName = "./env/";//想要存储文件的地址
    private static final String ansPathName = "./ans/";
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("admin/testcase/upload")
    public String uploadQuestion( MultipartFile initFile , MultipartFile ansFile ,
                                  MultipartFile extenFile , Integer questionId ) {
        if ( initFile.isEmpty( ) ) {
            return "error: init file is empty";
        } else if ( ansFile.isEmpty( ) ) {
            return "error: ans file is empty";
        }
        Question question = questionRepository.findById( questionId ).orElse( null );
        if ( question == null ) return "error: question not found";
        if ( initFile.getContentType( ) != null && !initFile.getContentType( ).equals( "text/sql" ) && !initFile.getContentType( ).equals( "text/x-sql" ) ) {
            return "error: initFile not sql file";
        }
        if ( ansFile.getContentType( ) != null && !ansFile.getContentType( ).equals( "text/sql" ) && !ansFile.getContentType( ).equals( "text/x-sql" ) ) {
            return "error: ansFile not sql file";
        }
        TestCase testCase = new TestCase( );
        try {
            IOUtil.fileStore( initFile, envPathName + questionId + ".sql" );
            IOUtil.fileStore( ansFile, ansPathName + questionId + ".sql" );
            testCase.setAnswerCode( Base64.getEncoder( ).encodeToString(ansFile.getBytes()) );
        } catch (IOException e) {
            e.printStackTrace( );
            return "error: " + e.getMessage( );
        }
        testCase.setQuestion( question );
        testCase.setInitDB( envPathName + questionId + ".sql" );
        if ( !extenFile.isEmpty( ) ) {
            try {
                testCase.setExtension( Base64.getEncoder( ).encodeToString(extenFile.getBytes()) );
            } catch (IOException e) {
                e.printStackTrace( );
                return "error: " + e.getMessage( );
            }
        }
        return "success: " + testCase.getId();
     }
}
