package com.grupo01.spring.service;

import java.util.List;

import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;

public interface CompraService {
	CompraResponse registrarCompra(CompraRequest compraRequest);

	List<CompraResponse> listarComprasPorCorreo(String mail);

}
