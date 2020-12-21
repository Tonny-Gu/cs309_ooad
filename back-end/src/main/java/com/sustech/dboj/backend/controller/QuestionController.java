package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.ContestRepository;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@Api(tags = "题目控制")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;


    @GetMapping("/question/id")
    @ApiOperation( value = "通过id获取题目")
    public Question getQuestionById( Integer id ) {
        Optional<Question> questionQuery = questionRepository.findById( id );
        return questionQuery.orElse( null );
    }


    @PostMapping("/question")
    @ApiOperation( value = "获取所有题目")
    public List<Question> getAllQuestion( Boolean withContent ) {
        List<Question> questionList = questionRepository.findAll( );
        if ( !withContent ) {
            for (Question q : questionList) {
                q.setContent( "" );
            }
        }
        return questionList;
    }


    @GetMapping("/question/contest")
    @ApiOperation( value = "获取某竞赛所有题目")
    public List<Question> getQuestionByContest( Integer contest_id ) {
        Contest contest = contestRepository.findById( contest_id ).orElse( null );
        if ( contest == null ) return null;
        return questionRepository.contestGetQuestions( contest_id );
    }

    @GetMapping("/question/name")
    @ApiOperation( value = "根据名字获取题目")
    public Question getQuestionByName( String name ) {
        return questionRepository.findByName( name );
    }

    @GetMapping("/question/author")
    @ApiOperation( value = "根据出题人获取题目")
    public List<Question> getQuestionByAuthor( Integer user_id ) {
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null ) return null;
        return questionRepository.findByAuthor( user );
    }

    @GetMapping("/question/degree")
    @ApiOperation( value = "根据难度获取题目")
    public List<Question> getQuestionByDegree( String degree ) {
        return questionRepository.findByDegree( degree );
    }

    @GetMapping("/question/db")
    @ApiOperation( value = "根据SQL类型获取题目")
    public List<Question> getQuestionByDbType( String dbType ) {
        return questionRepository.findByDbType( dbType );
    }



}
