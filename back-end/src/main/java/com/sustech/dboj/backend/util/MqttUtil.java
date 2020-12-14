package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
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
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.UUID;

@Configuration
public class MqttUtil {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JudgeLogRepository judgeLogRepository;

    @Autowired
    private MailServer mailServer;

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
            ObjectNode testCase = objectMapper.readValue( msg.getPayload( ) , ObjectNode.class );
            if(!testCase.get( "status" ).asText().equalsIgnoreCase( "success" )){
                String targetEmail = "11811905@mail.sustech.edu.cn";
                String alertMsg =  "[Sustech DBOJ] 李逸飞 同学, 你的测试样例上传失败，请找顾同舟击剑。";
                mailServer.sendEmail( targetEmail , "Password Modify" , alertMsg );
            }
            testCaseRepository.initEnv( testCase.get( "id" ).asInt() , testCase.get( "env" ).asText() );
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
            if ( node.has( "info" ) && node.has( "id" ) && node.has( "testCase" ) ) {
                String totInfo;
                String totStatus = "submit";
                Integer id = node.get( "id" ).asInt( );
                int caseLen = node.get( "testCase" ).size( );
                boolean pass = true;
                ObjectMapper infoMapper = new ObjectMapper( );
                ArrayNode root = infoMapper.createArrayNode( );
                for (int i = 0; i < caseLen; i++) {
                    String status = node.get( "testCase" ).get( i ).get( "status" ).asText( );
                    String info = node.get( "testCase" ).get( i ).get( "info" ).asText( );
                    JudgeLog judgeLog = new JudgeLog( );
                    judgeLog.setInfo( info );
                    judgeLogRepository.save( judgeLog );
                    if ( !status.equals( "Accept" ) ) {
                        pass = false;
                        totStatus = status;
                    }
                    ObjectNode nowCase = infoMapper.createObjectNode( );
                    nowCase.put( "id" , i );
                    nowCase.put( "status" , status );
                    root.add( nowCase );
                }
                totInfo = infoMapper.writeValueAsString( root );
                submissionRepository.updateInfo( id , totInfo , totStatus );
                // change score table
                Submission submission = submissionRepository.findById( id ).orElse( null );
                assert submission != null;
                Score now = scoreRepository.findByStudentAndQuestionAndContest( submission.getStudent( ) , submission.getQuestion( ) , submission.getContest( ) );
                now.setSubmit( now.getSubmit( ) + 1 );
                if ( pass && !now.getAc( ) ) {
                    now.setAc( true );
                    String acTime = node.get( "submitTime" ).asText( );
                    now.setAcTime( acTime );
                } else if ( !pass && !now.getAc( ) ) {
                    now.setWa( now.getWa( ) + 1 );
                }
                scoreRepository.save( now );
            }
        } );
    }

    @Bean
    public void mailListener() throws MqttException {
        String broker = "tcp://192.168.122.10:1883";
        String topic = "mail";
        String clientId = "mailListener";// use different clintID for different listener
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
            if ( node.has( "code" ) && node.has( "msg" ) ) {
                String code = node.get( "code" ).asText( );
                String message = node.get( "msg" ).asText( );
                String mailMsg = String.format( "[Warning] SQLitz 判题系统预警\n警告码[%s]\n预警信息: %s" , code , message );
                List<User> SAs = userRepository.findAllByRole( "ROLE_SA" );
                for (User sa : SAs) {
                    String targetEmail = sa.getUsername( ) + "@mail.sustech.edu.cn";
                    mailServer.sendEmail( targetEmail , "System Warning" , mailMsg );
                }
            }
        } );
    }
}
