package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sustech.dboj.backend.domain.Score;
import com.sustech.dboj.backend.domain.Submission;
import com.sustech.dboj.backend.domain.TestCase;
import com.sustech.dboj.backend.repository.ScoreRepository;
import com.sustech.dboj.backend.repository.SubmissionRepository;
import com.sustech.dboj.backend.repository.TestCaseRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MqttUtil {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ScoreRepository scoreRepository;


    private static final Logger logger = LoggerFactory.getLogger( MqttUtil.class );

    public static void sender( String broker , String topic , int qos , String message ) throws MqttException {
        String publisherId = UUID.randomUUID( ).toString( );
        MemoryPersistence persistence = new MemoryPersistence( );
        MqttClient publisher = new MqttClient( broker , publisherId , persistence );
        MqttConnectOptions connOpts = new MqttConnectOptions( );
        connOpts.setCleanSession( true );
        MqttMessage mqttMessage = new MqttMessage( );
        mqttMessage.setQos( qos );
        mqttMessage.setPayload( message.getBytes( ) );
        publisher.connect( );
        publisher.publish( topic , mqttMessage );
        publisher.disconnect( );
    }

    @Bean
    public void initListener() throws MqttException {
        String broker = "tcp://192.168.122.10:1883";
        String topic = "env/recv";
        String clientId = "initListener";
        MemoryPersistence persistence = new MemoryPersistence( );
        MqttClient sampleClient = null;
        sampleClient = new MqttClient( broker , clientId , persistence );
        MqttConnectOptions connOpts = new MqttConnectOptions( );
        connOpts.setCleanSession( true );
        logger.info( "Connecting to broker: " + broker );
        sampleClient.connect( connOpts );
        logger.info( broker + ":Connected Successful" );
        sampleClient.subscribe( topic , ( t , msg ) -> {
            // ... payload handling omitted
            logger.info( "topic: {} msg:{}" , t , msg );
            ObjectMapper objectMapper = new ObjectMapper( );
            TestCase testCase = objectMapper.readValue( msg.getPayload( ) , TestCase.class );
            testCaseRepository.initEnv( testCase.getId( ) , testCase.getEnv( ) );
        } );
    }

    @Bean
    public void submitListener() throws MqttException {
        String broker = "tcp://192.168.122.10:1883";
        String topic = "code/recv";
        String clientId = "submitListener";// use different clintID for different listener
        MemoryPersistence persistence = new MemoryPersistence( );
        MqttClient sampleClient = new MqttClient( broker , clientId , persistence );
        MqttConnectOptions connOpts = new MqttConnectOptions( );
        connOpts.setCleanSession( true );
        logger.info( "Connecting to broker: " + broker );
        sampleClient.connect( connOpts );
        logger.info( broker + ":Connected Successful" );
        sampleClient.subscribe( topic , ( t , msg ) -> {
            // ... payload handling omitted
            logger.info( "topic: {} msg: {}" , t , msg );
            ObjectNode node = new ObjectMapper( ).readValue( msg.getPayload( ) , ObjectNode.class );
            if ( node.has( "info" ) && node.has( "id" ) ) {
                String info = node.get( "info" ).asText( );
                String status = node.get( "status" ).asText( );//will del
                Integer id = node.get( "id" ).asInt( );
                submissionRepository.updateInfo( id , info , status );
                // change score table
                boolean pass = status.equals( "Accept" );
                Submission submission = submissionRepository.findById( id ).orElse( null );
                assert submission != null;
                Score now = scoreRepository.findByStudentAndQuestionAndContest( submission.getStudent( ) , submission.getQuestion( ) , submission.getContest( ) );
                now.setSubmit( now.getId( ) + 1 );
                if ( pass && !now.getAc( ) ) {
                    now.setAc( true );
                } else if ( !pass && !now.getAc( ) ) {
                    now.setWa( now.getWa( ) + 1 );
                }
                scoreRepository.save( now );
            }
        } );
    }
}
