package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.UserRepository;
import com.sustech.dboj.backend.util.TextChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/user")
    public String test1() {
        return "you are user";
    }

    @PostMapping("/admin")
    public String test2() {
        return "you are admin";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
