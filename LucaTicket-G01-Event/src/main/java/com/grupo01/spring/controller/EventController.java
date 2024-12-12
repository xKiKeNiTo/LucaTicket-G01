package com.grupo01.spring.controller;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.service.EventService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST para la gestión de eventos
 *
 * @version 1.1
 * @author equipo
 */
@RestController
@RequestMapping("/eventos")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> obtenerTodosEventos() {
		List<EventResponse> eventos = eventService.findAll();
		Map<String, Object> response = new HashMap<>();
		response.put("totalEventos", eventos.size());
		response.put("eventos", eventos);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/save")
	public ResponseEntity<EventResponse> saveEvent(@RequestBody @Valid EventRequest nuevoEvento) {
		try {
			EventResponse eventoGuardado = eventService.save(nuevoEvento);
			return ResponseEntity.status(HttpStatus.CREATED).body(eventoGuardado);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest()
					.body(new EventResponse("Error: Valor inválido para algún campo del evento."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new EventResponse("Error: Ocurrió un problema al guardar el evento."));
		}
	}

	@GetMapping("/detalles/{id}")
	public ResponseEntity<String> obtenerDetallesEvento(@PathVariable @Valid UUID id) {
		try {
			// Retrieve the event details using the service
			EventResponse evento = eventService.getReferenceById(id);

			// Convert the event details to a single-line string
			String detallesEvento = String.format("El evento '%s' se realiza en %s el dia %s a las %s",
					evento.getNombre(), evento.getLocalidad(), evento.getFechaEvento(), evento.getHoraEvento());

			return ResponseEntity.ok(detallesEvento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado para el ID proporcionado.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocurrió un problema al obtener los detalles del evento.");
		}
	}

	@GetMapping("/nombre")
	public ResponseEntity<Map<String, Object>> listarEventosPorNombre(@RequestParam String nombre) {
		List<EventResponse> eventos = eventService.findByNombreContainsIgnoreCase(nombre);
		Map<String, Object> response = new HashMap<>();
		response.put("totalEventos", eventos.size());
		response.put("eventos", eventos);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/edit")
	public ResponseEntity<Map<String, Object>> modificarEvento(@RequestParam UUID id,
			@RequestBody @Valid EventRequest eventoActualizado) {
		try {
			EventResponse eventoModificado = eventService.updateEvent(id, eventoActualizado);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Evento modificado correctamente");
			response.put("data", eventoModificado);

			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Error: El evento con el ID especificado no existe.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Error: Datos de entrada inválidos.");
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Error: Ocurrió un problema al modificar el evento.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
