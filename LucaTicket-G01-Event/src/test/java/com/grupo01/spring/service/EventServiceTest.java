package com.grupo01.spring.service;

import com.grupo01.spring.model.*;
import com.grupo01.spring.repository.EventDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

	@Mock
	private EventDao eventDao;

	@InjectMocks
	private EventService eventService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void debeGuardarEventoExitosamente() {
		EventRequest request = new EventRequest();
		request.setNombre("Concierto de Jazz");
		request.setDescripcion("Un concierto inolvidable");
		request.setFechaEvento(LocalDate.of(2024, 12, 25));
		request.setHoraEvento(LocalTime.of(20, 0));
		request.setPrecioMinimo(BigDecimal.valueOf(50.00));
		request.setPrecioMaximo(BigDecimal.valueOf(150.00));
		request.setLocalidad(Localidad.Madrid);
		request.setNombreRecinto("Palacio de Deportes");
		request.setGeneroMusica("Jazz");

		UUID generatedId = UUID.randomUUID();

		Event event = new Event();
		event.setId(generatedId); // Asignar un UUID explícito
		event.setNombre("Concierto de Jazz");

		when(eventDao.save(any(Event.class))).thenReturn(event);

		EventResponse response = eventService.save(request);

		assertNotNull(response);
		assertEquals("Concierto de Jazz", response.getNombre());
		assertEquals(generatedId, response.getId()); // Verificar que el ID coincide
		verify(eventDao, times(1)).save(any(Event.class));
	}

	@Test
	void debeMapearRequestToEntity() {
		// Datos de prueba
		EventRequest request = new EventRequest();
		request.setNombre("Concierto de Jazz");
		request.setDescripcion("Un concierto inolvidable");
		request.setFechaEvento(LocalDate.of(2024, 12, 25));
		request.setHoraEvento(LocalTime.of(20, 0));
		request.setPrecioMinimo(BigDecimal.valueOf(50.00));
		request.setPrecioMaximo(BigDecimal.valueOf(150.00));
		request.setLocalidad(Localidad.Madrid);
		request.setNombreRecinto("Palacio de Deportes");
		request.setGeneroMusica("Jazz");

		// Ejecutar método
		Event event = eventService.mapToEntity(request);

		// Verificar resultados
		assertEquals("Concierto de Jazz", event.getNombre());
		assertEquals(Localidad.Madrid, event.getLocalidad());
		assertEquals(LocalDate.of(2024, 12, 25), event.getFecha_evento());
		assertEquals(LocalTime.of(20, 0), event.getHora_evento());
	}

	@Test
	void debeMapearEntityToResponse() {
		UUID generatedId = UUID.randomUUID();

		Event event = new Event(generatedId, "Concierto de Rock", "Un concierto épico", LocalDate.of(2024, 12, 25),
				LocalTime.of(20, 0), BigDecimal.valueOf(50.00), BigDecimal.valueOf(150.00), Localidad.Madrid,
				"Wanda Metropolitano", "Rock");

		EventResponse response = eventService.mapToResponse(event);

		assertEquals(generatedId, response.getId()); // Comparar el UUID
		assertEquals("Concierto de Rock", response.getNombre());
		assertEquals(Localidad.Madrid, response.getLocalidad());
		assertEquals(LocalDate.of(2024, 12, 25), response.getFechaEvento());
		assertEquals(LocalTime.of(20, 0), response.getHoraEvento());
	}
}
