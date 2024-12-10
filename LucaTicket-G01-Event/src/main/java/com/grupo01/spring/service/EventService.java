package com.grupo01.spring.service;

import com.grupo01.spring.model.Event;
import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.EventDao;

public class EventService {

	private final EventDao eventDao;

	public EventService(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	/**
	 * Guarda un nuevo evento en la base de datos.
	 *
	 * @param eventRequest DTO con los datos del evento a crear.
	 * @return EventResponse DTO con los datos del evento creado.
	 */
	public EventResponse save(EventRequest eventRequest) {
		// Convertir EventRequest a la entidad Event
		Event event = mapToEntity(eventRequest);

		// Guardar la entidad Event en la base de datos
		Event savedEvent = eventDao.save(event);

		// Convertir la entidad guardada a EventResponse
		return mapToResponse(savedEvent);
	}

	/**
	 * Convierte un EventRequest a una entidad Event.
	 *
	 * @param request DTO de solicitud.
	 * @return Entidad Event.
	 */
	private Event mapToEntity(EventRequest request) {
		Event event = new Event();
		event.setNombre(request.getNombre());
		event.setDescripcion(request.getDescripcion());
		event.setFecha_evento(request.getFechaEvento());
		event.setHora_evento(request.getHoraEvento());
		event.setPrecio_minimo(request.getPrecioMinimo());
		event.setPrecio_maximo(request.getPrecioMaximo());
		event.setLocalidad(request.getLocalidad());
		event.setNombre_recinto(request.getNombreRecinto());
		event.setGenero_musical(request.getGeneroMusica());
		return event;
	}

	/**
	 * Convierte una entidad Event a un EventResponse.
	 *
	 * @param event Entidad Event.
	 * @return DTO EventResponse.
	 */
	private EventResponse mapToResponse(Event event) {
		EventResponse response = new EventResponse();
		response.setId(event.getId());
		response.setNombre(event.getNombre());
		response.setDescripcion(event.getDescripcion());
		response.setFechaEvento(event.getFecha_evento());
		response.setHoraEvento(event.getHora_evento());
		response.setPrecioMinimo(event.getPrecio_minimo());
		response.setPrecioMaximo(event.getPrecio_maximo());
		response.setLocalidad(event.getLocalidad());
		response.setNombreRecinto(event.getNombre_recinto());
		response.setGeneroMusica(event.getGenero_musical());
		return response;
	}

}
