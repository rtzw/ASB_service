package com.esp32.esp32Service;


import com.esp32.esp32Service.AuthorizedCard.AuthorizedCard;
import com.esp32.esp32Service.AuthorizedCard.AuthorizedCardService;
import com.esp32.esp32Service.Esp.Esp;
import com.esp32.esp32Service.Esp.EspService;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Service
public class MQTTHandler implements MqttCallback {

    private MqttClient client;
    private final EspService espService;
    private final AuthorizedCardService authorizedCardService;
    private HashMap<String, LocalDateTime> active = new HashMap<>();

    @Autowired
    public MQTTHandler(@Lazy EspService espService, AuthorizedCardService authorizedCardService) {
        this.espService = espService;
        this.authorizedCardService = authorizedCardService;
        try {
            Random random = new Random();
            client = new MqttClient("tcp://broker.emqx.io:1883", "clientid" +random.nextInt(100000));
            client.setCallback(this);
            MqttConnectOptions mqOptions = new MqttConnectOptions();
            mqOptions.setCleanSession(true);
            client.connect(mqOptions);
        } catch (MqttException e) {
            System.out.println("Error!");
            client = null;
        }
    }

    @Scheduled(fixedDelay = 8000)
    public void isActive(){
        ArrayList<Esp> allEsps = espService.getAllEsps();
        for(Esp esp: allEsps){
            if(!active.containsKey(esp.getId())){
                active.put(esp.getId(), LocalDateTime.now().minusDays(1));
            }
            if(active.get(esp.getId()).isBefore(LocalDateTime.now().minusSeconds(8))){
                esp.setIsEspOn(false);
                espService.save(esp);
            }

        }
    }
    public void addTopics(ArrayList<String> topics) throws MqttException {
        for(String topic: topics) {
            client.subscribe(topic);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection error");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {

        System.out.println(topic + " " + mqttMessage.toString());
        String[] topicParts = topic.split("/");
        Esp esp = espService.getEspById(topicParts[1]);
        if(!esp.getIsEspOn()) {
            esp.setIsEspOn(true);
        }
        active.put(esp.getId(), LocalDateTime.now());
        espService.save(esp);
        if(topicParts[2].equals("max")){
            if(topicParts[3].equals("temperature")){
                esp.setTemperature(Double.parseDouble(mqttMessage.toString()));
                espService.save(esp);
            }
        }
        else if(topicParts[2].equals("fan")){
            if(topicParts[3].equals("status")){
                esp.setIsFanOn(mqttMessage.toString().equals("on"));
                espService.save(esp);
            }
            else if(topicParts[3].equals("auto")){
                esp.setIsFanAuto(mqttMessage.toString().equals("on"));
                espService.save(esp);
            }
        }
        else if(topicParts[2].equals("newCard")){
            if(topicParts[3].equals("get")){
                AuthorizedCard authorizedCard = AuthorizedCard.builder().code(mqttMessage.toString())
                        .espId(topicParts[1]).build();
                try {
                    authorizedCardService.addNewCard(authorizedCard);
                } catch (Exception ignore){}
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}