package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.News;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.NewsRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "布告栏")
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String noticePathName = "notice/";

    @GetMapping("/notice/new")
    @ApiOperation( value = "获取最新公告")
    public News getCurrentNew() {
        List<News> enableNews = newsRepository.findCurrentNotice( );
        if ( enableNews.isEmpty( ) ) return null;
        return enableNews.get( 0 );
    }

    @GetMapping("/notice/all")
    @ApiOperation( value = "获取所有有效公告")
    public List<News> getEnableNew() {
        return newsRepository.findCurrentNotice( );
    }

    @PostMapping("/admin/notice/upload")
    @ApiOperation( value = "上传公告")
    public String uploadNew( String topic, String content , String author ) {
            User au = userRepository.findByUsername( author );
            if ( au == null ) return "error: invalid author";
            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper( );
            News notice = new News( );
            notice.setTopic( topic );
            notice.setContent( content );
            SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            notice.setTime( ft.format( new Date( ) ) );
            notice.setEnable( true );
            notice.setAuthor( au );
            newsRepository.save( notice );
            return "upload successful";
    }

    @PostMapping("/admin/notice/enable")
    @ApiOperation(value = "使某公告有效")
    public String enableNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null ) return "notice not found";
        newsRepository.activeNotice( Id , true );
        return String.format( "notice %d enable" , Id );
    }

    @PostMapping("/admin/notice/cancel")
    @ApiOperation(value = "取消某公告")
    public String cancelNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null ) return "notice not found";
        newsRepository.activeNotice( Id , false );
        return String.format( "notice %d cancel" , Id );
    }


}
