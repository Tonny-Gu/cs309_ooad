package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.MailServer;
import com.sustech.dboj.backend.util.SimpleUtil;
import com.sustech.dboj.backend.util.TextChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;

@Configuration
@RestController
@Api(tags = "用户操作")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    MailServer mailServer;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public String register( String username , String password , String name , String role ) {
        if ( !TextChecker.userNameChecker( username ) ) return "Invalid username";
        if ( userRepository.findByUsername( username ) != null ) return "Username have been used";
        if ( ( !role.contains( "TA" ) ) && ( !role.contains( "SA" ) ) && ( !role.contains( "STU" ) ) )
            return "Invalid role";
        if ( !TextChecker.passwordChecker( password ) ) return "Invalid password";
        User newUser = new User( );
        newUser.setName( name );
        newUser.setUsername( username );
        String[] authorities = role.split( "," );
        for (String e : authorities) {
            newUser.setRole( "ROLE_" + role );//坑：自己写的role要在前面加上ROLE_ 因为库里面自带的比较会在角色前面加上这个
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( );
        newUser.setPassword( encoder.encode( password ) );
        newUser.setNickname( name );
        userRepository.save( newUser );
        log.info( "User: {}, name: {} have been created" , newUser.getUsername( ) , newUser.getName( ) );
        return "Create user successful";
    }

    @GetMapping("/user/get/contests")
    @ApiOperation(value = "获取用户参与的所有contest")
    public List<Contest> getContests( Integer id ) {
        User user = userRepository.findById( id ).orElse( null );
        if ( user == null ) return null;
        return contestRepository.userGetContests( id );
    }

    @GetMapping("/user/get/submission")
    @ApiOperation(value = "获取用户所有提交")
    public List<Submission> getSubmissionsByUser( Integer id ) {
        User user = userRepository.findById( id ).orElse( null );
        if ( user == null ) return null;
        return submissionRepository.getLogByStu( id );
    }

    @PostMapping("/user/get/submission")
    @ApiOperation(value = "获取用户某赛题最近提交")
    public List<Submission> getSubmissions( Integer id , Integer contest_id , Integer question_id , Boolean recent ) {
//        User user = userRepository.findById( id ).orElse( null );
//        if ( user == null ) return null;
//        Question question = questionRepository.findById( id ).orElse( null );
//        if ( question == null ) return null;
//        Contest contest = contestRepository.findById( contest_id ).orElse( null );
//        if ( contest == null ) return null;
        List<Submission> submissionList = submissionRepository.getLog( id , question_id , contest_id );
        if ( recent ) {
            while (submissionList.size( ) > 1) {
                submissionList.remove( submissionList.size( ) - 1 );
            }
        }
        return submissionList;
    }

    @Transactional
    @PostMapping("/user/joinContest")
    @ApiOperation(value = "用户加入contest")
    public String joinContest( Integer user_id , Integer contest_id ) {
        // add contest-user
        User myUser = userRepository.findById( user_id ).orElse( null );
        Contest myContest = contestRepository.findById( contest_id ).orElse( null );
        if ( myUser == null ) return "User not found";
        if ( myContest == null ) return "Contest not found";
        userRepository.joinContest( user_id , contest_id );
        log.info( "User: {} join Contest: {}" , myUser.getUsername( ) , myContest.getName( ) );
        // create score table
        for (Question question : myContest.getQuestions( )) {
            Score score = new Score( );
            score.setContest( myContest );
            score.setQuestion( question );
            score.setStudent( myUser );
            scoreRepository.save( score );
        }
        return "Join contest successfully";
    }

    @PostMapping("/user/send/code")
    @ApiOperation(value = "发送验证码")
    public String sendCode( Integer user_id ) throws MessagingException {
        String activeCode = SimpleUtil.getRandomCode( 6 );
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null ) return "User Not Found";
        user.setActiveCode( activeCode );
        String targetEmail = user.getUsername( ) + "@mail.sustech.edu.cn";
        String msg = String.format( "[Sustech DBOJ] %s 同学, 你修改密码的验证码为 %s , 请勿泄露" , user.getName( ) , activeCode );
        mailServer.sendEmail( targetEmail , "Password Modify" , msg );
        userRepository.save( user );
        return "Sender Success";
    }

    @PostMapping("/user/modify/password")
    @ApiOperation(value = "修改密码")
    public String modifyCode( Integer user_id , String password, String code){
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null ) return "User Not Found";
        if(user.getActiveCode().equals( code )){
            user.setActiveCode( null );
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( );
            user.setPassword( encoder.encode( password ) );
            userRepository.save( user );
            return "Modify Success";
        }else{
            return "Invalid Code";
        }
    }

    @PostMapping("/user/modify/nickname")
    @ApiOperation(value = "修改花名")
    public String modifyNickName( Integer user_id , String nickname){
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null ) return "User Not Found";
        user.setNickname( nickname );
        userRepository.save( user );
        return "Modify Success";
    }



    @GetMapping("/index")
    public String index() {
        return "index with /";
    }

}
