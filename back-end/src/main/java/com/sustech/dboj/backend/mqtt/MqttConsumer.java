package com.sustech.dboj.backend.mqtt;

import com.sustech.dboj.backend.config.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;

//@Component
public class MqttConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttConsumer.class);

//    @Bean
//    @ServiceActivator(inputChannel = MqttConfig.INBOUND_CHANNEL)
    public MessageHandler handler() {
        return message -> {
            String topic = Objects.requireNonNull( message.getHeaders( ).get( MqttConfig.RECEIVED_TOPIC_KEY ) ).toString();
            LOGGER.info("[{}]topic receive message:{}", topic, message.getPayload().toString());
        };
    }
}
