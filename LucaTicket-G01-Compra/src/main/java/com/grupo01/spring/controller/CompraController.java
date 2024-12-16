package com.grupo01.spring.controller;

import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.service.CompraService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

	@Autowired
	private CompraService compraService;

	@PostMapping("/save")
	public ResponseEntity<CompraResponse> realizarCompra(@Valid @RequestBody CompraRequest compraRequest) {
		CompraResponse response = compraService.registrarCompra(compraRequest);
		return ResponseEntity.ok(response);
	}
	
    @GetMapping("/compras")
    public ResponseEntity<List<CompraResponse>> listarComprasPorMail(@RequestParam String mail) {
        List<CompraResponse> compras = compraService.listarComprasPorCorreo(mail);
        if (compras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(compras);
    }
	
}
