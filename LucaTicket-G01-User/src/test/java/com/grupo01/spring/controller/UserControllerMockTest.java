package com.grupo01.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
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
		mockMvc.perform(post("/users/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.mail").value(userResponse.getMail()))
				.andExpect(jsonPath("$.nombre").value(userResponse.getNombre()))
				.andExpect(jsonPath("$.apellido").value(userResponse.getApellido()))
				.andExpect(jsonPath("$.fechaAlta").value(userResponse.getFechaAlta().toString()));

		// Verificar que el servicio fue llamado correctamente
		Mockito.verify(userService).save(Mockito.any());
	}

	@Test
	public void debeDevolverUsuarioPorCorreo() throws Exception {
		// Simulación de datos del usuario
		String email = "test.user@example.com";
		UserResponse mockUserResponse = new UserResponse(email, "Test", "User", LocalDate.of(2023, 1, 1));

		// Configuración de Mock del servicio
		when(userService.findByMail(email)).thenReturn(mockUserResponse);

		// Petición al endpoint y verificaciones
		mockMvc.perform(get("/findByMail").param("mail", email).contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.mail").value(mockUserResponse.getMail()))
				.andExpect(jsonPath("$.nombre").value(mockUserResponse.getNombre()))
				.andExpect(jsonPath("$.apellido").value(mockUserResponse.getApellido()))
				.andExpect(jsonPath("$.fechaAlta").value(mockUserResponse.getFechaAlta().toString()));

		// Verifica que el servicio se llama exactamente una vez
		verify(userService, times(1)).findByMail(email);
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "correo-invalido", " " })
	public void debeDevolverBadRequestCuandoDatosSonInvalidos(String emailInvalido) throws Exception {
		mockMvc.perform(get("/findByMail").param("mail", emailInvalido).contentType("application/json"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());

		verify(userService, times(0)).findByMail(anyString());
	}
}
