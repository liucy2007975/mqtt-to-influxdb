package com.labaix;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by nicolas on 15/04/15.
 */
public class ApplicationMain {

    public static void main(String[] args) {

        //String broker       = "tcp://localhost:1883";
        String broker       = "tcp://192.168.0.9:1883";
        String clientId     = "JavaSample"+System.currentTimeMillis();
        try {
            MqttClient collecterClient = new MqttClient(broker, "collecter");
            collecterClient.connect();
            collecterClient.subscribe("#");
            collecterClient.setCallback(new MqttMessage());

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
