package com.grupo01.spring.feignClient.error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {
	@Bean
	ErrorDecoder errorDecoder() {
		return new BancoErrorDecoder();
	}
}
