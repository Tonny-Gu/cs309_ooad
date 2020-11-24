package com.sustech.dboj.backend.config;


import com.sustech.dboj.backend.controller.UserController;
import com.sustech.dboj.backend.domain.Contest;
import com.sustech.dboj.backend.repository.ContestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleConfig {
    private static final Logger logger = LoggerFactory.getLogger( ScheduleConfig.class );
    @Autowired
    private ContestRepository contestRepository;

    @Scheduled(cron = "1 0 0 * * ?")// cancel
    public void cancelContest() {
        SimpleDateFormat ft = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        String now = ft.format( new Date( ) );
        List<Contest> contestList = contestRepository.findAll( );
        for (Contest contest : contestList) {
            if ( contest.getEndTime( ).compareTo( now ) < 0 ) {
                contest.setEnable( false );
                contestRepository.save( contest );
                logger.info( "Contest:{} Expired" , contest.getId( ) );
            } else if ( contest.getBeginTime( ).compareTo( now ) < 0 ) {
                contest.setEnable( true );
                contestRepository.save( contest );
                logger.info( "Contest:{} Enabled" , contest.getId( ) );
            }
        }
    }

}
