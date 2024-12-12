package com.grupo01.spring.controller;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/nombre")
	public ResponseEntity<List<EventResponse>> listarEventosPorNombre(@RequestParam String nombre) {
		List<EventResponse> eventos = eventService.findByNombre(nombre);
		if (eventos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(eventos);
	}
	
	@DeleteMapping("/deleteEvent/{id}")
	public ResponseEntity<Map<String, Object>> deleteEventById(@PathVariable String id) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        UUID uuid = UUID.fromString(id); // Intentar convertir el ID a UUID
	        EventResponse eventoEliminado = eventService.deleteEventById(uuid);

	        response.put("statusCode", HttpStatus.OK.value());
	        response.put("message", "El evento ha sido eliminado correctamente.");
	        response.put("Evento eliminado", eventoEliminado);
	        // Devuelve el evento eliminado con mensaje y código
	        return ResponseEntity.ok(response);
	    } catch (Exception e) { // En caso de no encontrar el evento
	        response.put("statusCode", HttpStatus.NOT_FOUND.value());
	        response.put("message", "Evento no encontrado para eliminar.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	}	
	
}
