package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.FileUtil;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;


@RestController
@Api(tags = "管理员接口")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger( AdminController.class );
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;
    private final ContestRepository contestRepository;
    private final TestCaseRepository testCaseRepository;
    private final NewsRepository newsRepository;


    private static final String pathName = "questions/";

    private static final String ansPathName = "ans/";//path of standard code '.sql'

    public AdminController( UserRepository userRepository , SubmissionRepository submissionRepository , QuestionRepository questionRepository , ContestRepository contestRepository , TestCaseRepository testCaseRepository , NewsRepository newsRepository ) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
        this.contestRepository = contestRepository;
        this.testCaseRepository = testCaseRepository;
        this.newsRepository = newsRepository;
    }


    @Transactional
    @PostMapping("/admin/question/upload")
    @ApiOperation(value = "上传题目")
    public String uploadQuestion( MultipartFile questionFile , MultipartFile extenFile , MultipartFile ansFile , Integer author , String degree , String dbType ) throws JsonProcessingException {
        Question question = new Question( );
        if ( questionFile.isEmpty( ) ) {
            return "error: question file is empty";
        } else if ( ansFile.isEmpty( ) ) {
            return "error: ans file is empty";
        } else {
            User au = userRepository.findById( author ).orElse( null );
            if ( au == null ) return "error: invalid author";
            if ( !( degree.equalsIgnoreCase( "Hard" ) || degree.equalsIgnoreCase( "Mid" ) || degree.equalsIgnoreCase( "Easy" ) ) ) {
                return "error: degree error";
            }
            if ( !( dbType.equalsIgnoreCase( "SQLite" )
                    || dbType.equalsIgnoreCase( "MySQL" ) || dbType.equalsIgnoreCase( "PostgreSQL" ) ) ) {
                return "error: dbType error";
            }
            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper( );
            try {
                String fileName = Objects.requireNonNull( questionFile.getOriginalFilename( ) ).split( "\\." )[0].replace( " " , "-" );
                FileUtil.fileStore( questionFile , pathName +
                        fileName + ".md" );
                question.setContent( Base64.getEncoder( ).encodeToString( w2h.markdown2Html( questionFile.getInputStream( ) ).getBytes( StandardCharsets.UTF_8 ) ) );

                FileUtil.fileStore( ansFile , ansPathName + fileName + ".sql" );
                question.setAnswerCode( Base64.getEncoder( ).encodeToString( ansFile.getBytes( ) ) );
            } catch (Exception e) {
                e.printStackTrace( );
                return "error: " + e.getMessage( );
            }
            String questionName = questionFile.getOriginalFilename( ).split( "\\." )[0];
            if ( questionRepository.findByName( questionName ) != null ) return "error: duplicate question name";
            question.setName( questionName );
            question.setDegree( degree );
            question.setDbType( dbType );
            question.setAuthor( au );
            if ( extenFile != null && !extenFile.isEmpty( ) ) {
                try {
                    question.setExtension( new String( extenFile.getBytes( ) ) );
                } catch (IOException e) {
                    e.printStackTrace( );
                    return "error: " + e.getMessage( );
                }
            }
            questionRepository.save( question );

            return "success: " + question.getId( );
        }

    }

    @PostMapping("/admin/question/modify")
    @ApiOperation(value = "修改题目")
    public String modifyQuestion( Question question ) {
        questionRepository.save( question );
        return "Success: " + question.getId( );
    }

    @PostMapping("/admin/question/cancel")
    @ApiOperation(value = "删除题目")
    public String cancelQuestion( Question question ) {
        question.setEnable( false );
        questionRepository.save( question );
        // TODO: 删除关联的testcase
        return "Success: " + question.getId( );
    }

    @PostMapping("/admin/getUser")
    @ApiOperation(value = "通过用户名获取用户")
    public User getUser( String username ) {
        return userRepository.findByUsername( username );
    }

}
