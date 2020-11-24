package com.sustech.dboj.backend.mqtt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class MqttTest {

    @Resource
    private MqttSender mqttGateway;

    @RequestMapping("/sendMqtt")
    public String sendMqtt(){
        String  sendData = "12356";
        System.out.println("消息订阅"+sendData);
        mqttGateway.sendToMqtt(sendData,"hello");
        return "OK";
    }

}
