package com.grupo01.spring.service;

import com.grupo01.spring.model.HistoricoVentas;
import com.grupo01.spring.model.HistoricoVentasRequest;
import com.grupo01.spring.repository.HistoricoVentasRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoricoVentasServiceImpl implements HistoricoVentasService {

	private final HistoricoVentasRepository historicoVentasRepository;

	@Autowired
	public HistoricoVentasServiceImpl(HistoricoVentasRepository historicoVentasRepository) {
		this.historicoVentasRepository = historicoVentasRepository;
	}

	@Override
	public void guardarDatosHistoricos(HistoricoVentasRequest request) {
		// Convertir el request a la entidad
		HistoricoVentas historicoVentas = convertirAEntidad(request);

		// Guardar en la base de datos
		historicoVentasRepository.save(historicoVentas);
	}

	// Método privado para convertir DTO a entidad
	private HistoricoVentas convertirAEntidad(HistoricoVentasRequest request) {
		HistoricoVentas historicoVentas = new HistoricoVentas();
		historicoVentas.setIdEvento(request.getIdEvento());
		historicoVentas.setPrecioMedio(request.getPrecioPromedio());
		historicoVentas.setTimestamp(request.getTimestamp());
		return historicoVentas;
	}
	
}
