package com.sustech.dboj.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndApplication {
//nohup java -jar back-end-0.0.1-SNAPSHOT.jar >backend.log 2>&1 &
    public static void main( String[] args ) {
        SpringApplication.run( BackEndApplication.class , args );
    }

}
