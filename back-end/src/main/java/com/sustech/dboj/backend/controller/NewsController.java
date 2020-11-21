package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.News;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.NewsRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import com.sustech.dboj.backend.util.IOUtil;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String noticePathName = "notice/";

    @GetMapping("/notice/new")
    public News getCurrentNew() {
        List<News> enableNews = newsRepository.findCurrentNotice( );
        if ( enableNews.isEmpty( ) ) return null;
        return enableNews.get( 0 );
    }

    @GetMapping("/notice/all")
    public List<News> getEnableNew() {
        return newsRepository.findCurrentNotice( );
    }

    @PostMapping("/notice/upload")
    public String uploadNew( MultipartFile file , String author ) {
        if ( file.isEmpty( ) ) {
            return "error:file is empty";
        } else {
//            if ( file.getContentType( ) != null && !file.getContentType( ).equals( "text/markdown" ) ) {
//                return "error:not markdown file";
//            }
            User au = userRepository.findByUsername( author );
            if ( au == null ) return "error: invalid author";
            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper( );
            News notice = new News( );
            try {
                IOUtil.fileStore( file , noticePathName + Objects.requireNonNull( file.getOriginalFilename( ) ) );
                notice.setContent( Base64.getEncoder( ).encodeToString( w2h.markdown2Html( file.getInputStream( ) ).getBytes( StandardCharsets.UTF_8 ) ) );
            } catch (IOException e) {
                e.printStackTrace( );
                return "error:" + e.getMessage( );
            }
            notice.setTopic( file.getOriginalFilename( ).split( "\\." )[0] );
            SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
            notice.setTime( ft.format( new Date( ) ) );
            notice.setEnable( true );
            notice.setAuthor( au );
            newsRepository.save( notice );
            return "upload successful";
        }
    }

    @GetMapping("/notice/enable")
    public String enableNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null ) return "notice not found";
        newsRepository.activeNotice( Id , true );
        return String.format( "notice %d enable" , Id );
    }

    @GetMapping("/notice/cancel")
    public String cancelNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null ) return "notice not found";
        newsRepository.activeNotice( Id , false );
        return String.format( "notice %d cancel" , Id );
    }
}
