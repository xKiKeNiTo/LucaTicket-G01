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
import java.util.Arrays;
import java.util.List;
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

	@Test
	void debeListarEventosCorrectamente() {
		// Configurar datos de prueba
		Event evento1 = new Event();
		evento1.setId(UUID.randomUUID());
		evento1.setNombre("Concierto de Rock");
		evento1.setDescripcion("Evento musical en vivo");
		evento1.setFecha_evento(LocalDate.of(2024, 12, 15));
		evento1.setHora_evento(LocalTime.of(20, 0));
		evento1.setPrecio_minimo(BigDecimal.valueOf(50));
		evento1.setPrecio_maximo(BigDecimal.valueOf(120));
		evento1.setLocalidad(Localidad.Madrid);
		evento1.setNombre_recinto("Wanda Metropolitano");
		evento1.setGenero_musical("Rock");

		Event evento2 = new Event();
		evento2.setId(UUID.randomUUID());
		evento2.setNombre("Festival de Jazz");
		evento2.setDescripcion("Evento musical al aire libre");
		evento2.setFecha_evento(LocalDate.of(2024, 6, 10));
		evento2.setHora_evento(LocalTime.of(18, 0));
		evento2.setPrecio_minimo(BigDecimal.valueOf(30));
		evento2.setPrecio_maximo(BigDecimal.valueOf(100));
		evento2.setLocalidad(Localidad.Barcelona);
		evento2.setNombre_recinto("Parque de la Música");
		evento2.setGenero_musical("Jazz");

		when(eventDao.findAll()).thenReturn(Arrays.asList(evento1, evento2));

		// Llamar al método del servicio
		List<EventResponse> eventos = eventService.findAll();

		// Verificar los resultados
		assertEquals(2, eventos.size());
		assertEquals("Concierto de Rock", eventos.get(0).getNombre());
		assertEquals("Festival de Jazz", eventos.get(1).getNombre());

	}
}
