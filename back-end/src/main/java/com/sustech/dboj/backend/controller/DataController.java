package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.QuestionRepository;
import com.sustech.dboj.backend.repository.SubmissionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(tags = "数据收集")
public class DataController {
    private static final Logger log = LoggerFactory.getLogger( DataController.class );
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
                case "Accepted":
                    acCnt++;
                    break;
                case "Wrong Answer":
                    waCnt++;
                    break;
                case "Time Limited": // not sure
                    tleCnt++;
                    break;
            }
        }
        map.put( "Accepted" , acCnt );
        map.put( "Wrong Answer" , waCnt );
        map.put( "Waiting" , waitCnt );
        map.put( "Time Limited" , tleCnt );
        map.put( "totSubmit" , submitCnt );
        return map;
    }


    @PostMapping("/data/time/7d")
    @ApiOperation(value = "提交时间汇总(7d)")
    public Map<Object, Map<Object, Object>> getSubmissions7d() {
        String[] twoDay = getBeforeDay( new Date(  ), -7 );
        List<Object[]> submissionList = submissionRepository.dataBetweenTime7d( twoDay[0] , twoDay[1] );
        return getResultFromQuery( submissionList );
    }
    @PostMapping("/data/time/24h")
    @ApiOperation(value = "提交时间汇总(24h)")
    public Map<Object, Map<Object, Object>> getSubmissions24h() {
        String[] twoDay = getBeforeDay( new Date(  ), -1 );
        List<Object[]> submissionList = submissionRepository.dataBetweenTime24h( twoDay[0], twoDay[1] );
        return getResultFromQuery( submissionList );
    }

    @PostMapping("/user/data/question")
    @ApiOperation(value = "做题统计")
    public Map<String, Object> getQuestion( Integer user_id ) {
        Map<String, Object> map = new HashMap<>( );
        map.put( "done" , submissionRepository.doneQuestion( user_id ).get( 0 )[0] );
        map.put( "all" , questionRepository.getQuestionsNum( ).get( 0 )[0] );
        return map;
    }


    private String[] getBeforeDay(Date now, int before){
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Calendar c = Calendar.getInstance( );
        String nowDate = ft.format( now );
        c.setTime( now );
        c.add( Calendar.DATE , - before );
        Date d = c.getTime( );
        return new String[]{ft.format( d ), nowDate};
    }

    private Map<Object, Map<Object, Object>> getResultFromQuery(List<Object[]> submissionList){
        Map<Object, Map<Object, Object>> result = new HashMap<>(  );
        for(Object[] objects : submissionList){
            if(!result.containsKey( objects[0] )){
                Map<Object, Object> temp =  new HashMap<>(  );
                temp.put( objects[1], objects[2] );
                result.put( objects[0],  temp );
            }else{
                if(!result.get( objects[0] ).containsKey( objects[1] )){
                    result.get( objects[0] ).put( objects[1],objects[2] );
                }else{
                    log.info( "Unexpected error: repeat (status & count)" );
                }
            }
        }
        return result;
    }
}
