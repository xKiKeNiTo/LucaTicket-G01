package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.service.EventService;

@WebMvcTest(EventController.class)
public class EventControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private EventService eventService;
	
	/**
	 * Prueba que devuelve un error 400 si se le pasan datos de entrada inválidos para Event.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeDevolverBadRequestCuandoTieneCamposInvalidos() throws Exception {
	    // JSON con campos inválidos
	    String eventInvalidoJson = "{"
	            + "\"nombre\": \"\", "  
	            + "\"fechaEvento\": null, "  
	            + "\"horaEvento\": null, " 
	            + "\"precioMinimo\": null, "  
	            + "\"precioMaximo\": null, "  
	            + "\"localidad\": null"  
	            + "}";

	    mockMvc.perform(post("/eventos/save")
	            .content(eventInvalidoJson)
	            .contentType("application/json"))
	        .andExpect(status().isBadRequest()) // Esperamos error 400
	        .andExpect(jsonPath("$.errors[?(@.field == 'fechaEvento' && @.message == 'La fecha del evento no puede estar vacía')]").exists())
	        .andExpect(jsonPath("$.errors[?(@.field == 'horaEvento' && @.message == 'La hora del evento no puede estar vacía')]").exists())
	        .andExpect(jsonPath("$.errors[?(@.field == 'nombre' && @.message == 'El nombre del evento no puede estar vacío')]").exists())
	        .andExpect(jsonPath("$.errors[?(@.field == 'precioMinimo' && @.message == 'El precio mínimo del evento no puede estar vacío')]").exists())
	        .andExpect(jsonPath("$.errors[?(@.field == 'precioMaximo' && @.message == 'El precio máximo del evento no puede estar vacío')]").exists());
	}
}
