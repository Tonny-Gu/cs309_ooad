package com.sustech.dboj.backend.service;

import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository mapper;
    @Override
    public UserDetails loadUserByUsername( String username) throws UsernameNotFoundException {
        User userBean = mapper.findByUsername(username);
        if (userBean == null) {
            throw new UsernameNotFoundException("数据库中无此用户！");
        }
        return userBean;
    }
}
