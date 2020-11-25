package com.sustech.dboj.backend.Util;

import com.sustech.dboj.backend.util.MqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;

public class MqttTest {
    @Test
    void testSender() throws MqttException {
        String broker = "tcp://192.168.122.10:1883" ;
        String topic =  "test";
        int qos  = 2;
        String message = "gtz nb!";
        MqttUtil.sender( broker, topic, qos, message );
    }
    @Test
    void testListener() throws MqttException {
        String broker = "tcp://192.168.122.10:1883" ;
        String topic =  "test";
        int qos  = 2;
    }
}
