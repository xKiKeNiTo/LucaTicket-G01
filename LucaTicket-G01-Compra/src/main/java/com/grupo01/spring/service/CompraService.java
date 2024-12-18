package com.grupo01.spring.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import org.springframework.data.repository.query.Param;

public interface CompraService {
	CompraResponse registrarCompra(CompraRequest compraRequest);

	Map<String, Object> listarComprasPorCorreo(String mail);
}
