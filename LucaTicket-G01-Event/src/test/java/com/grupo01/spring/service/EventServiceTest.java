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
	private EventServiceImpl eventServiceImpl;

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

		EventResponse response = eventServiceImpl.save(request);

		assertNotNull(response);
		assertEquals("Concierto de Jazz", response.getNombre());
		assertEquals(generatedId, response.getId()); // Verificar que el ID coincide
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
		Event event = eventServiceImpl.mapToEntity(request);

		// Verificar resultados
		assertEquals("Concierto de Jazz", event.getNombre());
		assertEquals(Localidad.Madrid, event.getLocalidad());
		assertEquals(LocalDate.of(2024, 12, 25), event.getFechaEvento());
		assertEquals(LocalTime.of(20, 0), event.getHoraEvento());
	}

	@Test
	void debeMapearEntityToResponse() {
		UUID generatedId = UUID.randomUUID();

		Event event = new Event(generatedId, "Concierto de Rock", "Un concierto épico", LocalDate.of(2024, 12, 25),
				LocalTime.of(20, 0), BigDecimal.valueOf(50.00), BigDecimal.valueOf(150.00), Localidad.Madrid,
				"Wanda Metropolitano", "Rock");

		EventResponse response = eventServiceImpl.mapToResponse(event);

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
		evento1.setFechaEvento(LocalDate.of(2024, 12, 15));
		evento1.setHoraEvento(LocalTime.of(20, 0));
		evento1.setPrecioMinimo(BigDecimal.valueOf(50));
		evento1.setPrecioMaximo(BigDecimal.valueOf(120));
		evento1.setLocalidad(Localidad.Madrid);
		evento1.setNombreRecinto("Wanda Metropolitano");
		evento1.setGeneroMusical("Rock");

		Event evento2 = new Event();
		evento2.setId(UUID.randomUUID());
		evento2.setNombre("Festival de Jazz");
		evento2.setDescripcion("Evento musical al aire libre");
		evento2.setFechaEvento(LocalDate.of(2024, 6, 10));
		evento2.setHoraEvento(LocalTime.of(18, 0));
		evento2.setPrecioMinimo(BigDecimal.valueOf(30));
		evento2.setPrecioMaximo(BigDecimal.valueOf(100));
		evento2.setLocalidad(Localidad.Barcelona);
		evento2.setNombreRecinto("Parque de la Música");
		evento2.setGeneroMusical("Jazz");

		// Configurar el mock del DAO
		when(eventDao.findAll()).thenReturn(Arrays.asList(evento1, evento2));

		// Llamar al método del servicio
		List<EventResponse> eventos = eventServiceImpl.findAll();

		// Verificar los resultados
		assertEquals(2, eventos.size());
		assertEquals("Concierto de Rock", eventos.get(0).getNombre());
		assertEquals("Festival de Jazz", eventos.get(1).getNombre());

	}

	@Test
	void debeDevolverEventosPorNombre() {
		// Configurar datos de prueba
		String nombreBusqueda = "Jazz";
		Event evento1 = new Event();
		evento1.setId(UUID.randomUUID());
		evento1.setNombre("Concierto de Jazz");
		evento1.setDescripcion("Evento musical en vivo");
		evento1.setFechaEvento(LocalDate.of(2024, 12, 15));
		evento1.setHoraEvento(LocalTime.of(20, 0));
		evento1.setPrecioMinimo(BigDecimal.valueOf(50));
		evento1.setPrecioMaximo(BigDecimal.valueOf(120));
		evento1.setLocalidad(Localidad.Madrid);
		evento1.setNombreRecinto("Palacio de Deportes");
		evento1.setGeneroMusical("Jazz");

		Event evento2 = new Event();
		evento2.setId(UUID.randomUUID());
		evento2.setNombre("Festival de Jazz");
		evento2.setDescripcion("Evento al aire libre");
		evento2.setFechaEvento(LocalDate.of(2024, 6, 10));
		evento2.setHoraEvento(LocalTime.of(18, 0));
		evento2.setPrecioMinimo(BigDecimal.valueOf(30));
		evento2.setPrecioMaximo(BigDecimal.valueOf(100));
		evento2.setLocalidad(Localidad.Barcelona);
		evento2.setNombreRecinto("Parque de la Música");
		evento2.setGeneroMusical("Jazz");

		// Configurar el mock del DAO
		when(eventDao.findByNombreContainingIgnoreCase(nombreBusqueda)).thenReturn(Arrays.asList(evento1, evento2));

		// Llamar al método del servicio
		List<EventResponse> eventos = eventServiceImpl.findByNombreContainsIgnoreCase(nombreBusqueda);

		// Verificar los resultados
		assertEquals(2, eventos.size());
		assertEquals("Concierto de Jazz", eventos.get(0).getNombre());
		assertEquals("Festival de Jazz", eventos.get(1).getNombre());
	}

	@Test
	void debeDevolverListaVaciaCuandoNoHayEventos() {
		// Configurar el mock para que devuelva una lista vacía
		when(eventDao.findAll()).thenReturn(Arrays.asList());

		// Llamar al método del servicio
		List<EventResponse> eventos = eventServiceImpl.findAll();

		// Verificar los resultados
		assertNotNull(eventos, "La lista de eventos no debería ser nula.");
		assertTrue(eventos.isEmpty(), "La lista de eventos debería estar vacía.");
	}

}
