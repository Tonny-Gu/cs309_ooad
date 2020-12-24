package com.sustech.dboj.backend.Util;

import com.sustech.dboj.backend.BackEndApplication;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackEndApplication.class)
public class questionDoneTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    void test01(){
        Object testObj = submissionRepository.doneQuestion( 1 ).get( 0 )[0];
        Object testObj2 = questionRepository.getQuestionsNum( ).get( 0 )[0];
        System.out.println(testObj );
        System.out.println(testObj2 );
    }
}
