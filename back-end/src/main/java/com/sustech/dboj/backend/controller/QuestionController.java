package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import com.sustech.dboj.backend.util.MarkDown2HtmlWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;


@RestController
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/question/findById")
    public Question getQuestion( Integer id) {
        Question question;
        Optional<Question> questionQuery = questionRepository.findById( id );
        return questionQuery.orElse( null );
    }

//    @PostMapping("/question/upload")
//    public String Question( MultipartFile file , String author) {
//        if(file.isEmpty()){
//            return "error:file is empty";
//        }else {
//            MarkDown2HtmlWrapper w2h = new MarkDown2HtmlWrapper();
//            try {
//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(new File(
//                                Objects.requireNonNull( file.getOriginalFilename( ) ) )));
//                System.out.println(file.getName());
//                System.out.println(file.getName());
//                System.out.println(file.getName());
//                out.write(file.getBytes());
//                out.flush();
//                out.close();
//                Question question = new Question();
//                question.setName( file.getName() );
//                question.setAuthor( userRepository.findByUsername( author ) );
//                question.setContent( w2h.markdown2Html( file.getInputStream()) );
//                questionRepository.save( question );
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "error:" + e.getMessage();
//            }
//
//            return "upload successful!";
//        }
//
//    }

}
