package com.esp32.esp32Service.Esp;


import com.esp32.esp32Service.MQTTHandler;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EspService {

    private final EspRepository espRepository;
    private final MQTTHandler mqttHandler;

    @Autowired
    public EspService(EspRepository espRepository, MQTTHandler mqttHandler) {
        this.espRepository = espRepository;
        this.mqttHandler = mqttHandler;
    }

    public void addEsp(Esp esp) throws MqttException {
        Optional<Esp> espOptional = espRepository.getEspById(esp.getId());
        if(espOptional.isPresent()) throw new IllegalArgumentException("Error!");
        mqttHandler.addTopics(topicsCreator(esp.getId()));

        espRepository.save(esp);
    }

    public void save(Esp esp){
        espRepository.save(esp);
    }

    public Esp getEspById(String espId){
        Optional<Esp> esp = espRepository.getEspById(espId);
        if(esp.isEmpty()) throw new IllegalArgumentException("Error!");
        return esp.get();
    }

    @PostConstruct
    public void mqtt() throws MqttException {
        ArrayList<Esp> esps = espRepository.getAll();
        ArrayList<String> topics = new ArrayList<>();
         for(Esp esp: esps){
             topics.addAll(topicsCreator(esp.getId()));
             System.out.println(topics);
        }
         mqttHandler.addTopics(topics);
    }



    public void changeStatus(String espId, Boolean isFanOn, Boolean isFanAuto, Double temperature ){
        Esp esp = Esp.builder().id(espId).build();
        if(isFanAuto != null) esp.setIsFanAuto(isFanAuto);
        if(isFanOn != null) esp.setIsFanOn(isFanOn);
        if(temperature != null) esp.setTemperature(temperature);
        espRepository.save(esp);
    }

    private ArrayList<String> topicsCreator(String espId){
        ArrayList<String> topics = new ArrayList<>();
        topics.add("ptcpapesvrwe/" + espId + "/sensor/temperature");
        topics.add("ptcpapesvrwe/" + espId + "/sensor/humidity");
        topics.add("ptcpapesvrwe/" + espId + "/fan/status");
        topics.add("ptcpapesvrwe/" + espId + "/fan/auto");
        topics.add("ptcpapesvrwe/" + espId + "/max/temperature");
        topics.add("ptcpapesvrwe/" + espId + "/esp/status");
        topics.add("ptcpapesvrwe/" + espId + "/newCard/get");
        topics.add("ptcpapesvrwe/" + espId + "/newCard/status");
        return topics;
    }

    public ArrayList<Esp> getAllEsps(){
        return espRepository.getAll();
    }

    public void removeEsp(String espId){
        espRepository.deleteById(espId);
    }


}
