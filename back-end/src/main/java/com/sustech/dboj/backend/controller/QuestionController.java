package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.ContestRepository;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import com.sustech.dboj.backend.util.IOUtil;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );
    private static final String pathName = "questions/";

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ContestRepository contestRepository;


    @PostMapping("/question/get/id")
    public Question getQuestionById( Integer id ) {
        Optional<Question> questionQuery = questionRepository.findById( id );
        return questionQuery.orElse( null );
    }

    @GetMapping("/question/get/all")
    public List<Question> getAllQuestion() {
        return questionRepository.findAll( );
    }

    @PostMapping("/question/get/contest")
    public List<Question> getQuestionByContest(Integer contest_id)
    {
        Contest contest = contestRepository.findById( contest_id ).orElse( null );
        if(contest == null)return null;
        return questionRepository.contestGetQuestions( contest_id );
    }
    @PostMapping("/question/get/name")
    public List<Question> getQuestionByName(String name)
    {
        return questionRepository.findByName( name );
    }

    @PostMapping("/question/get/author")
    public List<Question> getQuestionByAuthor(Integer user_id)
    {
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null )return null;
        return questionRepository.findByAuthor( user );
    }

    @PostMapping("/question/get/degree")
    public List<Question> getQuestionByDegree(String degree)
    {
        return questionRepository.findByName( degree );
    }

    @PostMapping("/question/get/dbtype")
    public List<Question> getQuestionByDbType(String dbType)
    {
        return questionRepository.findByName( dbType );
    }

    @Transactional
    @PostMapping("admin/question/upload")
    public String uploadQuestion( MultipartFile questionFile , Integer author , String degree , String dbType ) {
        Question question = new Question( );
        if ( questionFile.isEmpty( ) ) {
            return "error: question file is empty";
        } else {
            if ( questionFile.getContentType( ) != null && !questionFile.getContentType( ).equals( "text/markdown" ) ) {
                return "error: not markdown file";
            }
            User au = userRepository.findById( author ).orElse( null );
            if ( au == null ) return "error: invalid author";
            if ( !( degree.equalsIgnoreCase( "Hard" ) || degree.equalsIgnoreCase( "Mid" ) || degree.equalsIgnoreCase( "Easy" ) ) ) {
                return "error:degree error";
            }
            if ( !( dbType.equalsIgnoreCase( "ALL" ) || dbType.equalsIgnoreCase( "SQLite" )
                    || dbType.equalsIgnoreCase( "MySQL" ) || dbType.equalsIgnoreCase( "PostgreSQL" ) ) ) {
                return "error:degree error";
            }
            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper( );
            try {
                IOUtil.fileStore( questionFile,  pathName+
                        Objects.requireNonNull( questionFile.getOriginalFilename( ) ));
                question.setContent( Base64.getEncoder( ).encodeToString( w2h.markdown2Html( questionFile.getInputStream( ) ).getBytes( StandardCharsets.UTF_8 ) ) );

            } catch (IOException e) {
                e.printStackTrace( );
                return "error: " + e.getMessage( );
            }
            question.setName( questionFile.getOriginalFilename( ).split( "\\." )[0] );
            question.setDegree( degree );
            question.setAuthor( au );
            questionRepository.save( question );
            return "success: " + question.getId();
        }

    }

}
