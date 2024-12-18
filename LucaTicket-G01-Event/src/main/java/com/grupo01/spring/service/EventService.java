package com.grupo01.spring.service;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.EventDao;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.events.Event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface EventService {

	/**
	 * Obtiene todos los eventos como una lista de DTOs.
	 *
	 * @return Lista de EventResponse.
	 */
	List<EventResponse> findAll();

	/**
	 * Guarda un nuevo evento en la base de datos.
	 *
	 * @param eventRequest DTO con los datos del evento a crear.
	 * @return EventResponse DTO con los datos del evento creado.
	 */
	EventResponse save(EventRequest eventRequest);

	List<EventResponse> findByNombreContainsIgnoreCase(String nombre);

	EventResponse getReferenceById(UUID id);

	EventResponse updateEvent(UUID id, EventRequest eventoActualizado);

	EventResponse deleteEventById(UUID id);

	void actualizarPrecios(UUID idEvento, BigDecimal precioMinimo, BigDecimal precioMaximo);

}
