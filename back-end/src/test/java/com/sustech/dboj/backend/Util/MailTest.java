package com.sustech.dboj.backend.Util;

import com.sustech.dboj.backend.BackEndApplication;
import com.sustech.dboj.backend.util.MailServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackEndApplication.class)
public class MailTest {
    @Autowired
    MailServer mailServer;

    @Test
    void test01() throws MessagingException {
        mailServer.sendEmail( "11811806@mail.sustech.edu.cn","ooad mail test", "中文测试，验证码：SWD13D" );
    }
}
