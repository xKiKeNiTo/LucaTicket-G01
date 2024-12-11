package com.grupo01.spring.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
<<<<<<< HEAD
import java.util.List;
=======
import java.util.UUID;
>>>>>>> feature

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.model.Event;
import com.grupo01.spring.model.EventRequest;

import com.grupo01.spring.model.Localidad;
import com.grupo01.spring.service.EventService;

@WebMvcTest(EventController.class)
public class EventControllerTest {

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
	
	/**
	 * Prueba que devuelve un error 400 si se le pasan datos de entrada inválidos para Event.
	 *
	 * @throws Exception Si ocurre un error durante la solicitud.
	 */
	@Test
	public void debeLlamarServicioEventosCuandoLlamoEndpint() throws Exception {
		List<Event> events = Arrays.asList(
				
		when(eventService.findAll()).thenReturn(events);

		mockMvc.perform(get("/eventos")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));

		verify(eventService, times(1)).findAll();
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
				.andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].nombre").value("Concierto de Rock"))
				.andExpect(jsonPath("$[0].descripcion").value("Evento musical en vivo"))
				.andExpect(jsonPath("$[0].localidad").value("Madrid"))
				.andExpect(jsonPath("$[1].nombre").value("Festival de Jazz"))
				.andExpect(jsonPath("$[1].descripcion").value("Evento musical al aire libre"))
				.andExpect(jsonPath("$[1].localidad").value("Barcelona"));
	}

}
