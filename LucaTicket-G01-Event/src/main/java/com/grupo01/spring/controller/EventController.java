package com.grupo01.spring.controller;

import com.grupo01.spring.model.EventRequest;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Controlador REST para la gestion de eventos
 * @version 1
 * @author All
 */
@RestController
@RequestMapping("/eventos")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Guarda un evento en la base de datos.
     *
     * @param nuevoEvento El juego que se desea guardar.
     * @return ResponseEntity con el EventResponseDTO.
     */
    @PostMapping("/save")
    public ResponseEntity<EventResponse> saveEvent(@RequestBody @Valid EventRequest nuevoEvento) {
        try {
            EventResponse eventoGuardado = eventService.save(nuevoEvento);
            return ResponseEntity.ok(eventoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new EventResponse("Error: Valor inválido para algún campo del evento."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EventResponse("Error: Ocurrió un problema al guardar el evento."));
        }
    }
}
