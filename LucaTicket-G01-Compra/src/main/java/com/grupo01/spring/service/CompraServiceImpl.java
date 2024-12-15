package com.grupo01.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo01.spring.model.Compra;
import com.grupo01.spring.repository.CompraRepository;

@Service
public class CompraServiceImpl implements CompraService {

	@Autowired
	private CompraRepository compraRepository;

	public Compra registrarCompra(Compra compra) {
		return compraRepository.save(compra);
	}

}
