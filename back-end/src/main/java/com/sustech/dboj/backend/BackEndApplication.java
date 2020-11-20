package com.sustech.dboj.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BackEndApplication {
    //mvn package -DskipTests
//nohup java -jar back-end-0.0.1-SNAPSHOT.jar >backend.log 2>&1 &
    public static void main( String[] args ) {
        SpringApplication.run( BackEndApplication.class , args );
    }

}
