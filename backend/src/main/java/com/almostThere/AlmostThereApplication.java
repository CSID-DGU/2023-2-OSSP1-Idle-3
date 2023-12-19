package com.almostThere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AlmostThereApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlmostThereApplication.class, args);
	}
}
