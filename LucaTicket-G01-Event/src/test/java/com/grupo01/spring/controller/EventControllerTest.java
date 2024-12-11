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
                + "\"fecha_evento\": null, "  
                + "\"hora_evento\": null, " 
                + "\"precio_minimo\": null, "  
                + "\"precio_maximo\": null, "  
                + "\"localidad\": null"  
                + "}";
        
        mockMvc.perform(post("/eventos/save")
        		.content(eventInvalidoJson)
        		.contentType("application/json"))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.errors").exists())
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El nombre del evento no puede estar vacío")))
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La fecha del evento no puede estar vacía")))
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La hora del evento no puede estar vacía")))
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El precio mínimo del evento no puede estar vacío")))
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("El precio máximo del evento no puede estar vacío")))
        	.andExpect(jsonPath("$.errors", org.hamcrest.Matchers.hasItem("La localidad del evento no puede estar vacía")));        	             	
	}
}
