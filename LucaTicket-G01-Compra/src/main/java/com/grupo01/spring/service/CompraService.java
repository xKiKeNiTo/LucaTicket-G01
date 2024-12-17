package com.grupo01.spring.service;

import java.util.Map;

import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;

public interface CompraService {
	CompraResponse registrarCompra(CompraRequest compraRequest);

	Map<String, Object> listarComprasPorCorreo(String mail);

}
