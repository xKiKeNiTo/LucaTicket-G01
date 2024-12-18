package com.grupo01.spring.service;

<<<<<<< HEAD
import com.grupo01.spring.model.HistoricoVentasRequest;

public interface HistoricoVentasService {

	void guardarDatosHistoricos(HistoricoVentasRequest request);
	
=======
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface HistoricoVentasService {
	BigDecimal calcularPrecioPromedioPorEvento(UUID idEvent);
>>>>>>> feature-moha
}
