package com.sustech.dboj.backend.mqtt;

import com.sustech.dboj.backend.config.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class MqttTest {


    private static final Logger logger = LoggerFactory.getLogger(MqttConsumer.class);
    @Autowired
    private MqttSender mqttSender;

    @RequestMapping("/send")
    private void send(String data){
        mqttSender.sendToMqtt(data);
    }

    @Bean
    @ServiceActivator(inputChannel = MqttConfig.INBOUND_CHANNEL)
    public MessageHandler handler() {
        return message -> {
            String topic = message.getHeaders().get(MqttConfig.RECEIVED_TOPIC_KEY).toString();
            logger.info("[{}]主题接收到消息:{}", topic, message.getPayload().toString());
        };
    }

}
