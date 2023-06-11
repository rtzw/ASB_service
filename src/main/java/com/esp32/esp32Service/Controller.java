package com.esp32.esp32Service;

import com.esp32.esp32Service.AuthorizedCard.AuthorizedCard;
import com.esp32.esp32Service.AuthorizedCard.AuthorizedCardService;
import com.esp32.esp32Service.Esp.Esp;
import com.esp32.esp32Service.Esp.EspService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController

public class Controller {
    private final AuthorizedCardService authorizedCardService;
    private final EspService espService;

    @Autowired
    public Controller(AuthorizedCardService authorizedCardService, EspService espService) {
        this.authorizedCardService = authorizedCardService;
        this.espService = espService;
    }

    @GetMapping("/esp/addEsp")
    public void addEsp(@RequestParam String espId) throws MqttException {
        espService.addEsp(Esp.builder().id(espId).isFanOn(false).temperature(22.0).isFanAuto(true).build());
    }

    @GetMapping("/esp/removeEsp")
    public void removeEsp(@RequestParam String espId) throws MqttException {
        espService.removeEsp(espId);
    }

    @GetMapping("/card/addNewCard")
    public void addNewCard(@RequestParam String espId, @RequestParam String code) {
        authorizedCardService.addNewCard(AuthorizedCard.builder().espId(espId).code(code).build());
    }
    @GetMapping("/card/isCardAuthorized")
    public Boolean isCardAuthorized(@RequestParam String espId, @RequestParam String code) {
        return authorizedCardService.isCardAuthorized(AuthorizedCard.builder().espId(espId).code(code).build());
    }

    @GetMapping(path = "/getEsp")
    public Esp getEsp(@RequestParam String espId) {
        return espService.getEspById(espId);
    }

    @GetMapping(path = "/changeStatus")
    public void changeStatus(@RequestParam String espId, Boolean isFanOn, Boolean isFanAuto, Double temperature ) {
        espService.changeStatus(espId, isFanOn, isFanAuto, temperature);
    }

    @GetMapping(path = "/getAllEsps")
    public ArrayList<Esp> getAllEsps() {
        return espService.getAllEsps();
    }
}
