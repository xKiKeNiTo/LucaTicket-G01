package com.grupo01.spring.service;

import com.grupo01.spring.model.Event;
import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.EventDao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

	private final EventDao eventDao;

	public EventServiceImpl(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@Override
	public List<EventResponse> findAll() {
		return eventDao.findAll().stream().map(this::mapToResponse).toList();
	}

	public String getReferenceById(UUID id) {
		EventResponse evento = mapToResponse(eventDao.getReferenceById(id));
		String detallesEvento = String.format("El evento '%s' se realiza en %s el dia %s a las %s", evento.getNombre(),
				evento.getLocalidad(), evento.getFechaEvento(), evento.getHoraEvento());
		return detallesEvento;
	}

	@Override
	public List<EventResponse> findByNombreContainsIgnoreCase(String nombre) {
//		Nombre exacto
//		return eventDao.findByNombre(nombre).stream().map(this::mapToResponse).toList();
		return eventDao.findByNombreContainingIgnoreCase(nombre).stream().map(this::mapToResponse).toList();
	}

	@Override
	@Transactional
	public EventResponse save(EventRequest eventRequest) {
		Event event = mapToEntity(eventRequest);
		Event savedEvent = eventDao.save(event);
		return mapToResponse(savedEvent);
	}

	Event mapToEntity(EventRequest request) {
		Event event = new Event();
		event.setNombre(request.getNombre());
		event.setDescripcion(request.getDescripcion());
		event.setFechaEvento(request.getFechaEvento());
		event.setHoraEvento(request.getHoraEvento());
		event.setPrecioMaximo(request.getPrecioMaximo());
		event.setPrecioMinimo(request.getPrecioMinimo());
		event.setLocalidad(request.getLocalidad());
		event.setNombreRecinto(request.getNombreRecinto());
		event.setGeneroMusical(request.getGeneroMusica());
		return event;
	}

	EventResponse mapToResponse(Event event) {
	    return new EventResponse(
	        event.getId(),
	        event.getNombre(),
	        event.getDescripcion(),
	        event.getFechaEvento(),
	        event.getHoraEvento(),
	        event.getPrecioMaximo(),
	        event.getPrecioMinimo(),
	        event.getLocalidad(),
	        event.getNombreRecinto(),
	        event.getGeneroMusical()
	    );
	}


	@Override
	public EventResponse updateEvent(UUID id, EventRequest eventoActualizado) {
		Event eventoExistente = eventDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("El evento no existe"));

		eventoExistente.setNombre(eventoActualizado.getNombre());
		eventoExistente.setDescripcion(eventoActualizado.getDescripcion());
		eventoExistente.setFechaEvento(eventoActualizado.getFechaEvento());
		eventoExistente.setHoraEvento(eventoActualizado.getHoraEvento());
		eventoExistente.setPrecioMinimo(eventoActualizado.getPrecioMinimo());
		eventoExistente.setPrecioMaximo(eventoActualizado.getPrecioMaximo());
		eventoExistente.setLocalidad(eventoActualizado.getLocalidad());
		eventoExistente.setNombreRecinto(eventoActualizado.getNombreRecinto());
		eventoExistente.setGeneroMusical(eventoActualizado.getGeneroMusica());

		Event eventoGuardado = eventDao.save(eventoExistente);
		return mapToResponse(eventoGuardado);
	}
	
	/**
	 * Elimina un evento de la base de datos dado su ID.
	 *
	 * @param id ID del evento a eliminar.
	 * @return El evento eliminado.
	 * @throws RuntimeException Si el evento no se encuentra.
	 */
	@Override
	@Transactional
	public EventResponse deleteEventById(UUID id) {
		Optional<Event> eventOptional = eventDao.findById(id);  // Busca el evento en la base de datos
		if (eventOptional.isPresent()) {
			Event eventoEliminado = eventOptional.get();
			eventDao.delete(eventoEliminado);  // Elimina el evento
			return mapToResponse(eventoEliminado);  // Mapea el evento a un EventResponse
		} else {
			throw new RuntimeException("Evento con ID " + id + " no encontrado para eliminar.");
		}
	}

}
