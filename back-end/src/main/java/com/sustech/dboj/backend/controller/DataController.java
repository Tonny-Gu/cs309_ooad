package com.sustech.dboj.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.SubmissionRepository;
import com.sustech.dboj.backend.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "数据收集")
public class DataController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/data/submission")
    @ApiOperation(value = "提交记录统计")
    public Map<String, Object> getSubmissions( Integer user_id ) {
        Map<String, Object> map = new HashMap<>( );
        User user = userRepository.findById( user_id ).orElse( null );
        if(user == null){
            map.put( "error","User not found" );
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
        map.put( "Wait" , waCnt );
        map.put( "Timeout" , tleCnt );
        map.put( "totSubmit" , submitCnt );
        return map;
    }
}
