package com.grupo01.spring.feignClient;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo01.spring.feignClient.error.FeignConfig;
import com.grupo01.spring.model.EventResponse;

@FeignClient(name = "event-service", configuration = FeignConfig.class)
public interface EventClient {
	
	@GetMapping("/eventos/detalles")
	EventResponse obtenerDetallesEvento(@RequestParam UUID id);
	
	@PutMapping("/eventos/actualizarPrecios")
    void actualizarPrecios(@RequestParam UUID idEvento,
                           @RequestParam BigDecimal precioMinimo,
                           @RequestParam BigDecimal precioMaximo);
}
