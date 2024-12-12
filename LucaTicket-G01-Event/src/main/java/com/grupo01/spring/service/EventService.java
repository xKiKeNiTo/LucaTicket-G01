package com.grupo01.spring.service;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import org.yaml.snakeyaml.events.Event;

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

	EventResponse getReferenceById(UUID id);

	List<EventResponse> findByNombre(String nombre);
}
