package com.labaix;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Serie;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class MqttMessage implements MqttCallback
{
    private static InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.59.103:8086", "root", "root");

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("delivery completed : " + token);
    }

    public void connectionLost(Throwable cause) {
        System.out.println("connection lost");
        cause.printStackTrace();
    }

    public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
        try {
            if (message != null) {
                // Ne pas enlever le trim car le message se termine par '\0' du C
                System.out.println("----------------");
                byte[] octets = message.getPayload();
                octets = Arrays.copyOf(octets, octets.length-1);
                System.out.println(octets);
                System.out.println("----------------");
                String msg = new String(octets);
                System.out.println(topic + " : [" + msg + "]");
                String serieName = topic.replace("/", "_");
                if (serieName.startsWith("_")) {
                    serieName = serieName.substring(1);
                }
                System.out.println(serieName + " ==> [" + msg + "]");
                Serie serie = new Serie.Builder(serieName)
                        .columns("value")
                        .values(new Integer(msg))
                        .build();

                this.influxDB.write("domotic", TimeUnit.MILLISECONDS, serie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
