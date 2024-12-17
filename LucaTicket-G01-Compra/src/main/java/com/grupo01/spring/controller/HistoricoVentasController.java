package com.grupo01.spring.controller;

import com.grupo01.spring.service.HistoricoVentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/ventas")
public class HistoricoVentasController{

	@Autowired
	private HistoricoVentasService historicoVentasService;
	
	@GetMapping("/media")
	public ResponseEntity<?> calcularPrecioPromedioPorEvento(@RequestParam UUID idEvent){
		BigDecimal response = historicoVentasService.calcularPrecioPromedioPorEvento(idEvent);
		return ResponseEntity.ok(response);
	}
}
