package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sustech.dboj.backend.domain.*;
import com.sustech.dboj.backend.repository.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Configuration
public class MqttUtil {
    private final static String[] topics = {"env/recv", "code/recv", "mail"};

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
        mqttMessage.setPayload( Base64.getEncoder().encode( message.getBytes() ) );
        publisher.connect( );
        publisher.publish( topic , mqttMessage );
        publisher.disconnect( );
    }

    @Bean
    public void initListener() {
        try {
            String broker = "tcp://192.168.122.10:1883";
            String topic = "env/recv";
            String clientId = "initListener";
            MemoryPersistence persistence = new MemoryPersistence( );
            MqttClient sampleClient;
            sampleClient = new MqttClient( broker , clientId , persistence );
            MqttConnectOptions connOpts = new MqttConnectOptions( );
            connOpts.setCleanSession( true );
            logger.info( "Connecting to broker: " + broker );
            sampleClient.connect( connOpts );
            logger.info( broker + ":Connected Successful" );
            ObjectMapper objectMapper = new ObjectMapper( );
            sampleClient.subscribe( topic , 2, ( t , msg ) -> {
                try {
                    // ... payload handling omitted
                    logger.info( "topic: {} msg:{}" , t , msg );
                    String msgInfo = new String( Base64.getDecoder( ).decode( msg.getPayload( ) ) );
                    ObjectNode testCase = objectMapper.readValue( msgInfo , ObjectNode.class );
                    if ( !testCase.get( "status" ).asText( ).equalsIgnoreCase( "success" ) ) {
                        String targetEmail = "11811905@mail.sustech.edu.cn";
                        String alertMsg = "[Sustech DBOJ] 李逸飞 同学, 你的测试样例上传失败，请找顾同舟击剑。";
                        mailServer.sendEmail( targetEmail , "Upload Failed" , alertMsg );
                    }
                    testCaseRepository.initEnv( testCase.get( "id" ).asInt( ) , testCase.get( "env" ).asText( ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
                } );
        } catch (Exception e) {
            initListener();

            logger.info( "Upload error: "  );
            e.printStackTrace();
        }
    }

    @Bean
    public void submitListener() {
        try {
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
            sampleClient.subscribe( topic , 2, ( t , msg ) -> {
                try {
                    // ... payload handling omitted
                    logger.info( "topic: {} msg: {}" , t , msg );
                    String msgInfo = new String( Base64.getDecoder( ).decode( msg.getPayload( ) ) );
                    ObjectNode node = new ObjectMapper( ).readValue( msgInfo , ObjectNode.class );
                    if ( node.has( "info" ) && node.has( "id" ) && node.has( "testCases" ) ) {
                        String totInfo;
                        String totStatus = "submit";
                        Integer id = node.get( "id" ).asInt( );
                        int caseLen = node.get( "testCases" ).size( );
                        boolean pass = true;
                        ObjectMapper infoMapper = new ObjectMapper( );
                        ArrayNode root = infoMapper.createArrayNode( );
                        for (int i = 0; i < caseLen; i++) {
                            String status = node.get( "testCases" ).get( i ).get( "status" ).asText( );
                            String info = node.get( "testCases" ).get( i ).get( "info" ).asText( );
                            if(info.contains( "time out" )) status = "Timout";
                            JudgeLog judgeLog = new JudgeLog( );
                            judgeLog.setInfo( info );
                            judgeLogRepository.save( judgeLog );
                            if ( !status.equals( "Accepted" ) ) {
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            } );

        } catch (MqttException e) {
            logger.info( e.getMessage( ) );
        }
    }

    @Bean
    public void mailListener() throws MqttException {
        try {
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
            sampleClient.subscribe( topic , 2, ( t , msg ) -> {
                // ... payload handling omitted
                logger.info( "topic: {} msg: {}" , t , msg );
                String msgInfo = new String( Base64.getDecoder( ).decode( msg.getPayload( ) ) );
                ObjectNode node = new ObjectMapper( ).readValue( msgInfo , ObjectNode.class );
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
        } catch (MqttException e) {
            logger.info( e.getMessage( ) );
        }
    }
}
