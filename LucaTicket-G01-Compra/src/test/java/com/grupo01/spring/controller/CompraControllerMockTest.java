package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.service.CompraService;

@WebMvcTest(CompraController.class)
public class CompraControllerMockTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CompraService compraService;
	
	@Test
	public void debeDevolverBadRequestCuandoLosDatosSonInvalidos() throws Exception {
		// JSON con campos inválidos
		String compraInvalidaJson = """
		        {"email": "",
	            "eventId": null,
	            "bancoRequest": {
	                "tarjeta": "",
	                "cvv": null
	            }
	        }
	        """;

		mockMvc.perform(post("/compras/save").content(compraInvalidaJson).contentType("application/json"))
					.andExpect(status().isBadRequest()) // Espera error 400
					.andExpect(jsonPath("$.errors").exists()); // Verifica que hay un campo de errores		            		     
	}

}
