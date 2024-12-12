package com.grupo01.spring.controller;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.service.EventService;
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

    /**
     * Obtener detalles de un evento a partir de un id
     *
     * @param id id
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see String
     */
    @GetMapping("/detalles")
    public ResponseEntity<String> obtenerDetallesEvento(@RequestParam UUID id) {
        try {
            // Obtiene los detalles del evento
            String detallesEvento = eventService.getReferenceById(id);
            return ResponseEntity.ok(detallesEvento);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Evento no encontrado para el ID proporcionado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un problema al obtener los detalles del evento.");
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

}
