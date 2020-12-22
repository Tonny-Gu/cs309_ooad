package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.SubmissionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(tags = "数据收集")
public class DataController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/user/data/submission")
    @ApiOperation(value = "提交记录统计")
    public Map<String, Object> getSubmissions( Integer user_id ) {
        Map<String, Object> map = new HashMap<>( );
        User user = userRepository.findById( user_id ).orElse( null );
        if ( user == null ) {
            map.put( "error" , "User not found" );
            return map;
        }
        List<Submission> submissionList = submissionRepository.getLogByStu( user_id );
        int acCnt = 0, waCnt = 0, tleCnt = 0, waitCnt = 0;
        int submitCnt = submissionList.size( );
        for (Submission submission : submissionList) {
            switch (submission.getStatus( )) {
                case "Submit":
                    waitCnt++;
                    break;
                case "Accept":
                    acCnt++;
                    break;
                case "Wrong Answer":
                    waCnt++;
                    break;
                case "Timout": // not sure
                    tleCnt++;
                    break;
            }
        }
        map.put( "Accept" , acCnt );
        map.put( "Wrong Answer" , waCnt );
        map.put( "Wait" , waitCnt );
        map.put( "Timeout" , tleCnt );
        map.put( "totSubmit" , submitCnt );
        return map;
    }


    @PostMapping("/data/time")
    @ApiOperation(value = "提交时间汇总")
    public List<String[]> getSubmissions() {
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Calendar c = Calendar.getInstance( );
        c.setTime( new Date( ) );
        String nowTime = ft.format( c.getTime( ) );
        c.add( Calendar.DATE , -7 );
        Date d = c.getTime( );
        String sevenAgo = ft.format( d );
        List<Submission> submissionList = submissionRepository.getSubmissionBetweenTime( sevenAgo , nowTime );
        List<String[]> result = new ArrayList<>( );
        for (Submission submission : submissionList) {
            result.add( new String[]{submission.getStatus( ) , submission.getSubmitTime( )} );
        }
        return result;
    }

    @PostMapping("/user/data/question")
    @ApiOperation(value = "做题统计")
    public Map<String, Object> getQuestion( Integer user_id ) {
        Map<String, Object> map = new HashMap<>( );
        map.put( "done" , submissionRepository.doneQuestion( user_id ).get( 0 )[0] );
        map.put( "all" , questionRepository.getQuestionsNum( ).get( 0 )[0] );
        return map;
    }
}
