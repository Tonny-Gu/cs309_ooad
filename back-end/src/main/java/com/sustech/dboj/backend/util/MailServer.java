package com.sustech.dboj.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServer {
    private static final Logger logger = LoggerFactory.getLogger( MailServer.class );

    private final JavaMailSender javaMailSender;



    @Value("SUSTechDBOJ<${spring.mail.username}>")
    private String emailUser;

    public MailServer( JavaMailSender javaMailSender ) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String target,String subject,String text) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper=new MimeMessageHelper(msg,true);
        msgHelper.setFrom(emailUser);
        msgHelper.setTo(target);
        msgHelper.setSubject(subject);
        msgHelper.setText(text);
        javaMailSender.send(msg);
        logger.info("Sending email to: "+target);
    }

//    public void sendEmailWithAttachedFile(String target,String subject,String text,String filename,String filepath) throws MessagingException {
//
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        MimeMessageHelper msgHelper=new MimeMessageHelper(msg,true);
//        System.out.println("sending email to: "+target);
//        msgHelper.setFrom(emailUser);
//        msgHelper.setTo(target);
//        msgHelper.setSubject(subject);
//        msgHelper.setText(text);
//
//        FileSystemResource file = new FileSystemResource(new File(filepath));
//        msgHelper.addAttachment(filename,file);
//
//        javaMailSender.send(msg);
//    }
}