package com.grupo01.spring.service;

import com.grupo01.spring.model.*;
import com.grupo01.spring.repository.HistoricoVentasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class HistoricoVentasServiceImpl implements HistoricoVentasService {

	private static final Logger logger = LoggerFactory.getLogger(HistoricoVentasServiceImpl.class);

	private final HistoricoVentasRepository historicoVentasRepository;

    public HistoricoVentasServiceImpl(HistoricoVentasRepository historicoVentasRepository) {
        this.historicoVentasRepository = historicoVentasRepository;
    }

    @Override
	public BigDecimal calcularPrecioPromedioPorEvento(UUID idEvent) {
		return historicoVentasRepository.calcularPrecioPromedioPorEvento(idEvent);
	}
}
