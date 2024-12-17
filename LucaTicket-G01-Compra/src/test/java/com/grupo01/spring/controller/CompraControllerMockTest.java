package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.service.CompraService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.grupo01.spring.service.CompraService;

@WebMvcTest(CompraController.class)
public class CompraControllerMockTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CompraService compraService;

	@Autowired
	private ObjectMapper objectMapper;

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

	@Test
	void debeListarComprasPorCorreoCuandoCorreoEsValido() throws Exception {
		// Arrange: Datos de entrada y respuesta esperada
		String correo = "usuario@example.com";
		CompraResponse compraResponse1 = new CompraResponse("Compra encontrada exitosamente", true,
				UUID.randomUUID().toString(), BigDecimal.valueOf(150.00));
		CompraResponse compraResponse2 = new CompraResponse("Compra encontrada exitosamente", true,
				UUID.randomUUID().toString(), BigDecimal.valueOf(200.00));

		List<CompraResponse> respuestaEsperada = List.of(compraResponse1, compraResponse2);

		// Configuración del mock del servicio
		Mockito.when(compraService.listarComprasPorCorreo(correo)).thenReturn(respuestaEsperada);

		// Act: Llamar al endpoint con el correo válido
		mockMvc.perform(get("/compras").param("mail", correo).contentType(MediaType.APPLICATION_JSON))
				// Assert: Validar la respuesta HTTP
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(respuestaEsperada.size()))
				.andExpect(jsonPath("$[0].transactionId").value(compraResponse1.getTransactionId()))
				.andExpect(jsonPath("$[0].amount").value(compraResponse1.getAmount()))
				.andExpect(jsonPath("$[1].transactionId").value(compraResponse2.getTransactionId()))
				.andExpect(jsonPath("$[1].amount").value(compraResponse2.getAmount()));

		// Verificar que el servicio fue invocado con el correo correcto
		Mockito.verify(compraService).listarComprasPorCorreo(correo);
	}
}