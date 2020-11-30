package com.sustech.dboj.backend.controller;

import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.domain.Question;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.User;
import com.sustech.dboj.backend.repository.ScoreRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "成绩表")
public class ScoreController {
    @Autowired
    private ScoreRepository scoreRepository;

//    @PostMapping("/score/update")
//    @ApiOperation( value = "更新成绩表[暂定]")
//    public Score updateScore( User student , Question question , Contest contest , String status ) {
//        boolean pass = status.equals( "AC" );
//        Score now = scoreRepository.findByStudentAndQuestionAndContest( student , question , contest );
//        now.setSubmit( now.getId( ) + 1 );
//        if ( pass && !now.getAc( ) ) {
//            now.setAc( true );
//        } else if ( !pass && !now.getAc( ) ) {
//            now.setWa( now.getWa( ) + 1 );
//        }
//        scoreRepository.save( now );
//        return now;
//    }

}
