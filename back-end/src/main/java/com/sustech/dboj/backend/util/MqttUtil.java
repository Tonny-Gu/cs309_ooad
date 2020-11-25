package com.sustech.dboj.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

public class MqttUtil {

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

    public static void listener( String broker , String topic , int qos ) throws MqttException {
        String clientId = "JavaServer";
        MemoryPersistence persistence = new MemoryPersistence( );
        MqttClient sampleClient = null;
        sampleClient = new MqttClient( broker , clientId , persistence );
        MqttConnectOptions connOpts = new MqttConnectOptions( );
        connOpts.setCleanSession( true );
        System.out.println( "Connecting to broker: " + broker );
        sampleClient.connect( connOpts );
        System.out.println( "Connected" );
        sampleClient.subscribe( topic , ( t , msg ) -> {
            byte[] payload = msg.getPayload( );
            // ... payload handling omitted
                System.out.print("topic: " + t);
        } );
    }
}
