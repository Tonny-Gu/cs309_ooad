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
    public String register( String username, String password, String name, String role) {
        User newUser = new User();
        newUser.setName( name );
        newUser.setUsername( username );
        if((!role.equals( "TA" )) &&(!role.equals( "SA" ))&&(!role.equals( "STU" )))return "Invalid role";
        newUser.setRole( role );
        if( !TextChecker.passwordChecker( password ) )return "Invalid user password";
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        newUser.setPassword( encoder.encode( password ) );
        userRepository.save(newUser);
        return "Create user successful";
    }

    @PostMapping("/login")
    public User login( String username, String password) {
        User user = userRepository.findByUsername( username );
        if(user==null)return null;
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        if(encoder.matches( user.getPassword(),encoder.encode( password )))return user;
        else return null;
    }
    @GetMapping("/user/authtest")
    public String test1() {
        return "you are user";
    }
    @PostMapping("/admin/authtest")
    public String test2() {
        return "you are admin";
    }

}
