package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.News;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.NewsRepository;
import com.sustech.dboj.backend.repository.UserRepository;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/notice/new")
    public News getCurrentNew() {
        List<News> enableNews = newsRepository.findByEnableTrueOrderByIdDesc( );
        if ( enableNews.isEmpty( ) ) return null;
        return enableNews.get( 0 );
    }

    @GetMapping("/notice/all")
    public List<News> getEnableNew() {
        return newsRepository.findByEnableTrueOrderByIdDesc( );
    }

    @PostMapping("/notice/upload")
    public String uploadNew( MultipartFile file , String author ) {
        if ( file.isEmpty( ) ) {
            return "error:file is empty";
        } else {
            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper( );
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream( new File(
                                Objects.requireNonNull( file.getOriginalFilename( ) ) ) ) );
                System.out.println( file.getName( ) );
                if ( file.getContentType( ) != null && !file.getContentType( ).equals( "text/markdown" ) ) {
                    return "error:not markdown file";
                }
                out.write( file.getBytes( ) );
                out.flush( );
                out.close( );
                News notice = new News( );
                notice.setTopic( file.getOriginalFilename( ).split( "\\." )[0] );
                User au = userRepository.findByUsername( author );
                System.out.println( "author Id = " + au.getId( ) );
                notice.setAuthor( au );
                notice.setContent( w2h.markdown2Html( file.getInputStream( ) ) );
                notice.setEnable( true );
                SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
                notice.setTime( ft.format( new Date( ) ) );
                newsRepository.save( notice );
            } catch (IOException e) {
                e.printStackTrace( );
                return "error:" + e.getMessage( );
            }

            return "upload successful";
        }
    }

    @GetMapping("/notice/enable")
    public String enableNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null )return "notice not found";
        newsRepository.activeNotice( Id, true );
        return String.format( "notice %d enable", Id );
    }

    @GetMapping("/notice/cancel")
    public String cancelNew( Integer Id ) {
        News notice = newsRepository.findById( Id ).orElse( null );
        if ( notice == null )return "notice not found";
        newsRepository.activeNotice( Id, false );
        return String.format( "notice %d cancel", Id );
    }
}
