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
	void debeGuardarEventoCorrectamente() {
		// Datos de prueba
		EventRequest request = new EventRequest();
		request.setNombre("Concierto de Jazz");
		request.setDescripcion("Un concierto inolvidable");
		request.setFechaEvento(LocalDate.of(2024, 12, 25));
		request.setHoraEvento(LocalTime.of(20, 0));
		request.setPrecioMinimo(BigDecimal.valueOf(50.00));
		request.setPrecioMaximo(BigDecimal.valueOf(150.00));
		request.setLocalidad(Localidad.Albacete);
		request.setNombreRecinto("Palacio de Deportes");
		request.setGeneroMusica("Jazz");

		Event event = new Event();
		event.setId(null);
		event.setNombre("Concierto de Jazz");
 
		// Configurar mocks
		when(eventDao.save(any(Event.class))).thenReturn(event);

		// Ejecutar método
		EventResponse response = eventService.save(request);

		// Verificar resultados
		assertNotNull(response);
		assertEquals("Concierto de Jazz", response.getNombre());
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
		request.setLocalidad(Localidad.Albacete);
		request.setNombreRecinto("Palacio de Deportes");
		request.setGeneroMusica("Jazz");

		// Ejecutar método
		Event event = eventService.mapToEntity(request);

		// Verificar resultados
		assertEquals("Concierto de Jazz", event.getNombre());
		assertEquals(Localidad.Albacete, event.getLocalidad());
		assertEquals(LocalDate.of(2024, 12, 25), event.getFecha_evento());
		assertEquals(LocalTime.of(20, 0), event.getHora_evento());
	}

	@Test
	void debeMapearEntityToRequest() {
		// Datos de prueba
		Event event = new Event(null, "Concierto de Rock", "Un concierto épico", LocalDate.of(2024, 12, 25),
				LocalTime.of(20, 0), BigDecimal.valueOf(50.00), BigDecimal.valueOf(150.00), Localidad.Albacete,
				"Wanda Metropolitano", "Rock");

		// Ejecutar método
		EventResponse response = eventService.mapToResponse(event);

		// Verificar resultados
		assertEquals(1L, response.getId());
		assertEquals("Concierto de Rock", response.getNombre());
		assertEquals(Localidad.Albacete, response.getLocalidad());
		assertEquals(LocalDate.of(2024, 12, 25), response.getFechaEvento());
		assertEquals(LocalTime.of(20, 0), response.getHoraEvento());
	}
}
