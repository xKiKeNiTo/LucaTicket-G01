package com.grupo01.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(UserControllerMockTest.MockConfig.class)
public class UserControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void debeCrearUsuario() throws Exception {
		String usuarioValido = """
				{
				  "mail": "test@mail.com",
				  "nombre": "John",
				  "apellido": "Doe",
				  "contrasena": "password123"
				}""";

		Mockito.when(userService.save(Mockito.any()))
				.thenReturn(new UserResponse("test@mail.com", "John", "Doe", LocalDate.of(2024, 12, 17)));

		mockMvc.perform(post("/users/save").content(usuarioValido).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	void debeDevolverUsuarioPorCorreo() throws Exception {
		String email = "test@example.com";
		UserResponse userResponse = new UserResponse(email, "John", "Doe", LocalDate.now());

		Mockito.when(userService.findByMail(Mockito.eq(email))).thenReturn(userResponse);

		mockMvc.perform(get("/users/findByMail").param("mail", email).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.mail").value(userResponse.getMail()))
				.andExpect(jsonPath("$.nombre").value(userResponse.getNombre()))
				.andExpect(jsonPath("$.apellido").value(userResponse.getApellido()))
				.andExpect(jsonPath("$.fechaAlta").value(userResponse.getFechaAlta().toString()));

		Mockito.verify(userService).findByMail(email);
	}

	@Configuration
	static class MockConfig {
		@Bean
		public UserService userService() {
			return Mockito.mock(UserService.class);
		}
	}
}
