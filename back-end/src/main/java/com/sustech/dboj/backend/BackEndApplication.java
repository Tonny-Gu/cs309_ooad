package com.sustech.dboj.backend;

import com.sustech.dboj.backend.util.MqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BackEndApplication {
    //mvn package -DskipTests
    //nohup java -Dfile.encoding=utf-8 -jar back-end-0.0.1-SNAPSHOT.jar >backend.log 2>&1 &
    public static void main( String[] args ) {
        SpringApplication.run( BackEndApplication.class , args );
    }

}
