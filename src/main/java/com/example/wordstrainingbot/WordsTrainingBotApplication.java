package com.example.wordstrainingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WordsTrainingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordsTrainingBotApplication.class, args);
	}

}
