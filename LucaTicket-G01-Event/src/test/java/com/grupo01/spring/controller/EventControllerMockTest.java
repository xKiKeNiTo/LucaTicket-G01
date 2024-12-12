package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.model.EventResponse;

import com.grupo01.spring.model.Localidad;
import com.grupo01.spring.service.EventService;
import com.grupo01.spring.service.EventServiceImpl;

@WebMvcTest(EventController.class)
public class EventControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EventService eventService;

	/**
	 * Prueba que devuelve un error 400 si se le pasan datos de entrada inválidos
	 * para Event.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeDevolverBadRequestCuandoTieneCamposInvalidos() throws Exception {
		// JSON con campos inválidos
		String eventInvalidoJson = "{" + "\"nombre\": \"\", " + "\"fechaEvento\": null, " + "\"horaEvento\": null, "
				+ "\"precioMinimo\": null, " + "\"precioMaximo\": null, " + "\"localidad\": null" + "}";

		mockMvc.perform(post("/eventos/save").content(eventInvalidoJson).contentType("application/json"))
				.andExpect(status().isBadRequest()) // Esperamos error 400
				.andExpect(jsonPath(
						"$.errors[?(@.field == 'fechaEvento' && @.message == 'La fecha del evento no puede estar vacía')]")
						.exists())
				.andExpect(jsonPath(
						"$.errors[?(@.field == 'horaEvento' && @.message == 'La hora del evento no puede estar vacía')]")
						.exists())
				.andExpect(jsonPath(
						"$.errors[?(@.field == 'nombre' && @.message == 'El nombre del evento no puede estar vacío')]")
						.exists())
				.andExpect(jsonPath(
						"$.errors[?(@.field == 'precioMinimo' && @.message == 'El precio mínimo del evento no puede estar vacío')]")
						.exists())
				.andExpect(jsonPath(
						"$.errors[?(@.field == 'precioMaximo' && @.message == 'El precio máximo del evento no puede estar vacío')]")
						.exists());
	}

	@Test
	public void debeLlamarServicioEventosCuandoLlamoEndpoint() throws Exception {
		List<EventResponse> events = Arrays.asList(
				new EventResponse(UUID.randomUUID(), "Concierto de Rock", "Un gran concierto",
						LocalDate.of(2024, 12, 15), LocalTime.of(20, 30), new BigDecimal("50.00"),
						new BigDecimal("20.00"), Localidad.AlcalaDeHenares, "Recinto A", "Rock"),
				new EventResponse(UUID.randomUUID(), "Festival de Jazz", "Un festival de música jazz",
						LocalDate.of(2024, 12, 20), LocalTime.of(18, 0), new BigDecimal("60.00"),
						new BigDecimal("25.00"), Localidad.ACoruna, "Recinto B", "Jazz"));

		// Configurar el mock para que cuando se llame a findAll(), devuelva la lista de
		// eventos
		when(eventService.findAll()).thenReturn(events);

		// Realizar la solicitud GET al endpoint /eventos
		mockMvc.perform(get("/eventos/all").contentType("application/json")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalEventos").value(2)) // Verifica el número total de eventos
				.andExpect(jsonPath("$.eventos", org.hamcrest.Matchers.hasSize(2))) // Verifica que hay 2 eventos en el
																					// array
				.andExpect(jsonPath("$.eventos[0].nombre").value("Concierto de Rock")) // Verifica el primer evento
				.andExpect(jsonPath("$.eventos[1].nombre").value("Festival de Jazz")); // Verifica el segundo evento

	}

	@Test
	public void debeDevolverListaDeEventosCorrectamente() throws Exception {
		// Configurar datos de prueba
		EventResponse evento1 = new EventResponse();
		evento1.setId(UUID.randomUUID());
		evento1.setNombre("Concierto de Rock");
		evento1.setDescripcion("Evento musical en vivo");
		evento1.setFechaEvento(LocalDate.of(2024, 12, 15));
		evento1.setHoraEvento(LocalTime.of(20, 0));
		evento1.setPrecioMinimo(BigDecimal.valueOf(50));
		evento1.setPrecioMaximo(BigDecimal.valueOf(120));
		evento1.setLocalidad(Localidad.Madrid);
		evento1.setNombreRecinto("Wanda Metropolitano");
		evento1.setGeneroMusica("Rock");

		EventResponse evento2 = new EventResponse();
		evento2.setId(UUID.randomUUID());
		evento2.setNombre("Festival de Jazz");
		evento2.setDescripcion("Evento musical al aire libre");
		evento2.setFechaEvento(LocalDate.of(2024, 6, 10));
		evento2.setHoraEvento(LocalTime.of(18, 0));
		evento2.setPrecioMinimo(BigDecimal.valueOf(30));
		evento2.setPrecioMaximo(BigDecimal.valueOf(100));
		evento2.setLocalidad(Localidad.Barcelona);
		evento2.setNombreRecinto("Parque de la Música");
		evento2.setGeneroMusica("Jazz");

		// Simular la respuesta del servicio
		when(eventService.findAll()).thenReturn(Arrays.asList(evento1, evento2));

		// Realizar la solicitud y validar la respuesta
		mockMvc.perform(get("/eventos/all").contentType("application/json")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalEventos").value(2)) // Verifica que hay 2 eventos en total
				.andExpect(jsonPath("$.eventos", org.hamcrest.Matchers.hasSize(2))) // Verifica el tamaño de la lista de
																					// eventos
				.andExpect(jsonPath("$.eventos[0].nombre").value("Concierto de Rock"))
				.andExpect(jsonPath("$.eventos[1].nombre").value("Festival de Jazz"));
	}

	@Test
	public void debeDevolverEventosPorNombre() throws Exception {
		// Configurar datos de prueba
		EventResponse evento1 = new EventResponse();
		evento1.setId(UUID.randomUUID());
		evento1.setNombre("Concierto de Rock");
		evento1.setDescripcion("Evento musical en vivo");
		evento1.setFechaEvento(LocalDate.of(2024, 12, 15));
		evento1.setHoraEvento(LocalTime.of(20, 0));
		evento1.setPrecioMinimo(BigDecimal.valueOf(50));
		evento1.setPrecioMaximo(BigDecimal.valueOf(120));
		evento1.setLocalidad(Localidad.Madrid);
		evento1.setNombreRecinto("Wanda Metropolitano");
		evento1.setGeneroMusica("Rock");

		EventResponse evento2 = new EventResponse();
		evento2.setId(UUID.randomUUID());
		evento2.setNombre("Festival de Rock");
		evento2.setDescripcion("Otro evento musical");
		evento2.setFechaEvento(LocalDate.of(2024, 6, 10));
		evento2.setHoraEvento(LocalTime.of(18, 0));
		evento2.setPrecioMinimo(BigDecimal.valueOf(30));
		evento2.setPrecioMaximo(BigDecimal.valueOf(100));
		evento2.setLocalidad(Localidad.Barcelona);
		evento2.setNombreRecinto("Estadio Olímpico");
		evento2.setGeneroMusica("Rock");

		// Simular la respuesta del servicio
		when(eventService.findByNombreContainsIgnoreCase("Rock")).thenReturn(Arrays.asList(evento1, evento2));

		// Realizar la solicitud y validar la respuesta
		mockMvc.perform(get("/eventos/nombre").param("nombre", "Rock").contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.totalEventos").value(2)) // Verifica el número total
																							// de eventos
				.andExpect(jsonPath("$.eventos", org.hamcrest.Matchers.hasSize(2))) // Verifica que hay 2 eventos en la
																					// lista
				.andExpect(jsonPath("$.eventos[0].nombre").value("Concierto de Rock")) // Verifica el nombre del primer
																						// evento
				.andExpect(jsonPath("$.eventos[1].nombre").value("Festival de Rock")); // Verifica el nombre del segundo
																						// evento
	}

}
