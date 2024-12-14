package com.grupo01.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LucaTicketG01UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(LucaTicketG01UserApplication.class, args);
	}

}
