package com.grupo01.spring.service;

import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;

public interface CompraService {
    CompraResponse registrarCompra(CompraRequest compraRequest);
}

