package com.sustech.dboj.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    BackdoorAuthenticationProvider backdoorAuthenticationProvider;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        //将自定义验证类注册进去,加入后门
        auth.authenticationProvider( backdoorAuthenticationProvider );
        //加入数据库验证类，下面的语句实际上在验证链中加入了一个DaoAuthenticationProvider
        auth.userDetailsService( myUserDetailsService ).passwordEncoder( new BCryptPasswordEncoder( ) );
    }
//
//    @Bean
//    RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl( );
//        hierarchy.setHierarchy( "TA > SA SA > STU" );
//        return hierarchy;
//    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.cors( )
                .and( )
                .authorizeRequests( )
                .antMatchers( "/user/**" ).hasAnyRole( "STU" , "TA" , "SA" )
                .antMatchers( "/admin/**" ).hasAnyRole( "TA" , "SA" )
                .anyRequest( ).permitAll( )
//                .and()
//                .antMatchers("/**").hasIpAddress("127.0.0.1")
                .and( )
                .formLogin( ).loginPage( "/login" )
                //以下是两个json接口的回调，用于处理登陆成功和失败的情况
                .successHandler( ( req , resp , authentication ) -> {
                    Object principal = authentication.getPrincipal( );
                    resp.setContentType( "application/json;charset=utf-8" );
                    PrintWriter out = resp.getWriter( );
                    out.write( new ObjectMapper( ).writeValueAsString( principal ) );
                    out.flush( );
                    out.close( );
                } )
                .failureHandler( ( req , resp , e ) -> {
                    resp.setContentType( "application/json;charset=utf-8" );
                    PrintWriter out = resp.getWriter( );
                    out.write( e.getMessage( ) );
                    out.flush( );
                    out.close( );
                } )
                //登出的设置
                .and( )
                .logout( )
                .logoutUrl( "/logout" )
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler( ( req , resp , authentication ) -> {
                    resp.setContentType( "application/json;charset=utf-8" );
                    PrintWriter out = resp.getWriter( );
                    out.write( "log out successful" );
                    out.flush( );
                    out.close( );
                } )
                .permitAll( )
                .and( )
//                .and( )
                .csrf( ).disable( ).exceptionHandling( )
                .authenticationEntryPoint( ( req , resp , authException ) -> {
                    resp.setContentType( "application/json;charset=utf-8" );
                    PrintWriter out = resp.getWriter( );
                    out.write( "Not logged in" );
                    out.flush( );
                    out.close( );
                } );
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource( );
        CorsConfiguration configuration = new CorsConfiguration( );
        configuration.setAllowCredentials( true );
        configuration.setAllowedOrigins( Collections.singletonList( "*" ) );
        configuration.setAllowedMethods( Collections.singletonList( "*" ) );
        configuration.setAllowedHeaders( Collections.singletonList( "*" ) );
        configuration.applyPermitDefaultValues( );
        configuration.setMaxAge( Duration.ofHours( 1 ) );
        source.registerCorsConfiguration( "/**" , configuration );
        return source;
    }

}
