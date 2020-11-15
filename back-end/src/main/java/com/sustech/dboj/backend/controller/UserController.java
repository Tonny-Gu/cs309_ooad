package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import com.sustech.dboj.backend.util.TextChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRepository contestRepository;

    @PostMapping("/register")
    public String register( String username , String password , String name , String role ) {
        if ( !TextChecker.userNameChecker( username ) ) return "Invalid username";
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
        return "Create user successful";
    }

    @GetMapping("/user/getinfo")
    public User getinfo( Integer id) {
        Optional<User> userQuery = userRepository.findById( id );
        return userQuery.orElse( null );
    }

    @PostMapping("/user/joinContest")
    public String joinContest(Integer user_id, Integer contest_id) {
        Optional<User> userQuery = userRepository.findById( user_id );
        User myUser = userQuery.orElse( null );

        Optional<Contest> contestQuery = contestRepository.findById( contest_id );
        Contest myContest = contestQuery.orElse( null );
        if ( myUser == null )return "User not found";
        if ( myContest == null )return "Contest not found";

        myContest.getUsers().add( myUser );
        myUser.getContests().add( myContest );

        userRepository.save( myUser );
        contestRepository.save( myContest );

        return "Join contest successfully";
    }



    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
