package com.grupo01.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.service.CompraService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
@Import(CompraControllerMockTest.MockConfig.class)
public class CompraControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CompraService compraService;

	@Autowired
	private ObjectMapper objectMapper;

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

	@Configuration
	public class MockConfig {

		@Bean
		public CompraService compraService() {
			return Mockito.mock(CompraService.class);
		}
	
	}

}
