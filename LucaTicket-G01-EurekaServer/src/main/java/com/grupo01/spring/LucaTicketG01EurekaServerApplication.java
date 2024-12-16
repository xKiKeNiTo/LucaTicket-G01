package com.grupo01.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LucaTicketG01EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LucaTicketG01EurekaServerApplication.class, args);
	}

}
