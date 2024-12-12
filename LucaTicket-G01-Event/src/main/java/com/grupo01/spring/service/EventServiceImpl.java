package com.grupo01.spring.service;

import com.grupo01.spring.model.Event;
import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.repository.EventDao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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


	public EventResponse getReferenceById(UUID id) {
		return mapToResponse(eventDao.getReferenceById(id));
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
		event.setPrecioMinimo(request.getPrecioMinimo());
		event.setPrecioMaximo(request.getPrecioMaximo());
		event.setLocalidad(request.getLocalidad());
		event.setNombreRecinto(request.getNombreRecinto());
		event.setGeneroMusical(request.getGeneroMusica());
		return event;
	}

	EventResponse mapToResponse(Event event) {
		return new EventResponse(event.getId(), event.getNombre(), event.getDescripcion(), event.getFechaEvento(),
				event.getHoraEvento(), event.getPrecioMinimo(), event.getPrecioMaximo(), event.getLocalidad(),
				event.getNombreRecinto(), event.getGeneroMusical());
	}
}
