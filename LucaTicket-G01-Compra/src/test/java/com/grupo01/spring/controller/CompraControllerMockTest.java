package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grupo01.spring.service.CompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


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

    @Test
    public void debeRegistrarCompraCuandoLosDatosSonValidos() throws Exception {
        // JSON con campos validos
        String compraValida = """
                {
                  "email": "kike.verac@gmail.com",
                  "eventId": "2b6f46b1-66f7-4157-9831-6c10551e2116",
                  "bancoRequest": {
                    "nombreTitular": "Enrique Vera",
                    "numeroTarjeta": "4111111111111111",
                    "mesCaducidad": "12",
                    "yearCaducidad": "2025",
                    "cvv": "123",
                    "emisor": "Visa",
                    "concepto": "Compra de entradas",
                    "cantidad": 50.0
                  }
                }""";

        mockMvc.perform(post("/compras/save").content(compraValida).contentType("application/json"))
                .andExpect(status().isOk());
    }
}
