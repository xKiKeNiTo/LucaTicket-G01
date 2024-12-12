package com.grupo01.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerMockTest.MockConfig.class) // Importar configuración personalizada
public class UserControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void debeCrearUsuario() throws Exception {
		// Datos de entrada para la prueba
		UserRequest userRequest = new UserRequest("test@example.com", "John", "Doe", "password123");

		// Respuesta esperada del servicio
		UserResponse userResponse = new UserResponse("test@example.com", "John", "Doe", LocalDate.of(2024, 12, 12));

		// Configurar el mock del servicio
		Mockito.when(userService.save(Mockito.any())).thenReturn(userResponse);

		// Ejecutar la solicitud POST al endpoint
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.mail").value(userResponse.getMail()))
				.andExpect(jsonPath("$.nombre").value(userResponse.getNombre()))
				.andExpect(jsonPath("$.apellido").value(userResponse.getApellido()))
				.andExpect(jsonPath("$.fechaAlta").value(userResponse.getFechaAlta().toString()));

		// Verificar que el servicio fue llamado correctamente
		Mockito.verify(userService).save(Mockito.any());
	}

	// Clase de configuración para registrar manualmente el mock de UserService
	@Configuration
	static class MockConfig {
		@Bean
		public UserService userService() {
			return Mockito.mock(UserService.class);
		}
	}

}
