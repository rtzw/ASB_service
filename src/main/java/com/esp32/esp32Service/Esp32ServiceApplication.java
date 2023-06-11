package com.esp32.esp32Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Esp32ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Esp32ServiceApplication.class, args);
	}

}
