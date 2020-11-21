package com.sustech.dboj.backend.config;


import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleConfig {

    @Autowired
    private ContestRepository contestRepository;

    @Scheduled(cron = "10 0 0 * * ?")// cancel
    public void cancelContest() {
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        String now = ft.format( new Date( ) );
        List<Contest> contestList = contestRepository.findAll( );
        for (Contest contest : contestList) {
            if ( contest.getEndTime( ).compareTo( now ) < 0 ) {
                contest.setEnable( false );
                contestRepository.save( contest );
                System.out.printf( "Contest:%d Expired" , contest.getId( ) );
            }
        }
    }

}
