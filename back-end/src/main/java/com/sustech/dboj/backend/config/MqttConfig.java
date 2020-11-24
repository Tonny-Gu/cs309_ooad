package com.sustech.dboj.backend.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    public static final String OUTBOUND_CHANNEL = "outboundChannel";
    public static final String INBOUND_CHANNEL = "inboundChannel";

    public static final String RECEIVED_TOPIC_KEY = "mqtt_receivedTopic";

    private static final String username = "username";
    private static final String password = "password";
    private static final String[] serverURIs = new String[]{"tcp://ip:port"};
    private static final String clientId = "id";
    private static final int keepAliveInterval = 30;
    private static final int connectionTimeout = 30;

    private static final int defaultProducerQos = 1;
    private static final boolean defaultRetained = true;
    private static final String defaultTopic = "defaultTopicName";

    private static final int defaultConsumerQos = 1;
    private static final long completionTimeout = 30000;
    private static final String[] consumerTopics = new String[]{"topic1" , "topic2"};

    /* 客户端 */
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions( );
        mqttConnectOptions.setUserName( username );
        mqttConnectOptions.setPassword( password.toCharArray( ) );
        mqttConnectOptions.setServerURIs( serverURIs );
        mqttConnectOptions.setKeepAliveInterval( keepAliveInterval );
        mqttConnectOptions.setConnectionTimeout( connectionTimeout );

        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory getMqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory( );
        factory.setConnectionOptions( getMqttConnectOptions( ) );

        return factory;
    }

    /* 发布者 */

    @Bean
    public MessageChannel outboundChannel() {
        return new DirectChannel( );
    }

    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler getMqttProducer() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler( clientId + "_producer" , getMqttClientFactory( ) );
        messageHandler.setAsync( true );
        messageHandler.setDefaultTopic( defaultTopic );
        messageHandler.setDefaultRetained( defaultRetained );
        messageHandler.setDefaultQos( defaultProducerQos );

        return messageHandler;
    }

    /* 订阅者 */

    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel( );
    }

    @Bean
    public MessageProducer getMqttConsumer() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter( clientId + "_consumer" , getMqttClientFactory( ) , consumerTopics );
        adapter.setCompletionTimeout( completionTimeout );
        adapter.setConverter( new DefaultPahoMessageConverter( ) );
        adapter.setQos( defaultConsumerQos );
        adapter.setOutputChannel( inboundChannel( ) );

        return adapter;
    }
}