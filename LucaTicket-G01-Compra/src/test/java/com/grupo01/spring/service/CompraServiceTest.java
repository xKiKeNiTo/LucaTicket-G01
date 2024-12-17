package com.grupo01.spring.service;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.hibernate.metamodel.model.domain.AnyMappingDomainType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.feignClient.EventClient;
import com.grupo01.spring.feignClient.UserClient;
import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.BancoResponse;
import com.grupo01.spring.model.Compra;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.CompraRepository;

/**
 * Clase CompraServiceTest Para probar las diferentes interacciones 
 * de la capa Service de Compras. 
 * 16/12/2024
 * @version 1
 * @author raul_
 */
@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {

	@Mock
	private CompraRepository compraRepository;

	@Mock
	private BancoClient bancoClient;

	@Mock
	private UserClient userClient;

	@Mock
	private EventClient eventClient;

	@InjectMocks
	private CompraServiceImpl compraService;

	@Test
	void debeManejarErrorCuandoCompraNoEsValida() {
		// Datos de prueba
		CompraRequest compraRequest = new CompraRequest();
		UUID eventId = UUID.randomUUID();
		compraRequest.setEmail("test@ejemplo.com");
		compraRequest.setEventId(eventId);

		BancoRequest bancoRequest = new BancoRequest();
		bancoRequest.setNumeroTarjeta("1234-5678-1234-5678");
		bancoRequest.setCvv("123");
		bancoRequest.setCantidad(BigDecimal.valueOf(100));
		compraRequest.setBancoRequest(bancoRequest);

		String token = "mocked-token";

		// Mock de los clientes
		when(bancoClient.autenticarUsuario(anyMap())).thenReturn(Map.of("token", token));
		when(userClient.getUserByEmail("test@ejemplo.com"))
				.thenReturn(new UserResponse("test@ejemplo.com", "Test", "User", "12/12/2012"));

		// Mock del cliente de eventos
		EventResponse mockEventResponse = new EventResponse();
		mockEventResponse.setPrecioMinimo(BigDecimal.valueOf(50));
		mockEventResponse.setPrecioMaximo(BigDecimal.valueOf(150));
		when(eventClient.obtenerDetallesEvento(eq(eventId))).thenReturn(mockEventResponse);

		// Simula respuesta inválida del banco
		when(bancoClient.validarCompra(eq(bancoRequest), eq("Bearer " + token)))
				.thenThrow(new RuntimeException("El banco rechazó la compra"));

		// Verifica comportamiento esperado
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			compraService.registrarCompra(compraRequest);
		});

		// Verificar el mensaje de la excepción
		assertEquals("El banco rechazó la compra", exception.getMessage());

		// Verificar interacciones
		verify(bancoClient).autenticarUsuario(anyMap());
		verify(userClient).getUserByEmail("test@ejemplo.com");
		verify(eventClient).obtenerDetallesEvento(compraRequest.getEventId()); // Verifica llamada al cliente de eventos
		verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
		verifyNoInteractions(compraRepository); // No debe guardar en la base de datos si hay error
	}

	@Test
	void debeConsumirMicroservicioBancoCorrectamente() {
		// Datos de prueba
		UUID eventId = UUID.randomUUID();
		CompraRequest compraRequest = new CompraRequest();
		compraRequest.setEmail("test@ejemplo.com");
		compraRequest.setEventId(eventId);

		BancoRequest bancoRequest = new BancoRequest();
		bancoRequest.setNumeroTarjeta("1234-5678-1234-5678");
		bancoRequest.setCvv("123");
		bancoRequest.setCantidad(BigDecimal.valueOf(100));
		compraRequest.setBancoRequest(bancoRequest);

		String token = "mocked-token";
		BancoResponse bancoResponse = new BancoResponse();
		bancoResponse.setCodigo("123456");
		bancoResponse.setMensaje("Compra realizada con éxito");
		bancoResponse.setSuccess(true);

		UserResponse userResponse = new UserResponse("test@ejemplo.com", "Test", "User", "12/12/2012");
		EventResponse eventResponse = new EventResponse();
		eventResponse.setPrecioMinimo(BigDecimal.valueOf(50));
		eventResponse.setPrecioMaximo(BigDecimal.valueOf(150));

		// Mock del flujo de datos
		when(bancoClient.autenticarUsuario(anyMap())).thenReturn(Map.of("token", token));
		when(userClient.getUserByEmail("test@ejemplo.com")).thenReturn(userResponse);
		when(eventClient.obtenerDetallesEvento(eq(eventId))).thenReturn(eventResponse);
		when(bancoClient.validarCompra(eq(bancoRequest), eq("Bearer " + token))).thenReturn(bancoResponse);

		// Llama al método del servicio
		CompraResponse response = compraService.registrarCompra(compraRequest);

		// Verifica el resultado
		assertNotNull(response);
		assertTrue(response.isSuccess());
		assertEquals("Compra realizada con éxito", response.getMessage());
		assertEquals("123456", response.getTransactionId());
		assertNotNull(response.getAmount());
		assertTrue(response.getAmount().compareTo(eventResponse.getPrecioMinimo()) >= 0);
		assertTrue(response.getAmount().compareTo(eventResponse.getPrecioMaximo()) <= 0);

		// Verifica interacciones
		verify(bancoClient).autenticarUsuario(anyMap());
		verify(userClient).getUserByEmail("test@ejemplo.com");
		verify(eventClient).obtenerDetallesEvento(eq(eventId));
		verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
	}

	@Test
	void debeRegistrarCompraCorrectaemnte() {
		// Datos de prueba
		CompraRequest compraRequest = new CompraRequest();
		UUID eventId = UUID.randomUUID();
		compraRequest.setEmail("test@ejemplo.com");
		compraRequest.setEventId(eventId);

		BancoRequest bancoRequest = new BancoRequest();
		bancoRequest.setNumeroTarjeta("1234-5678-1234-5678");
		bancoRequest.setCvv("123");
		bancoRequest.setCantidad(BigDecimal.valueOf(100));
		compraRequest.setBancoRequest(bancoRequest);

		String token = "mocked-token";

		// Mock de BancoResponse
		BancoResponse bancoResponse = new BancoResponse();
		bancoResponse.setCodigo("123456");
		bancoResponse.setMensaje("Compra validada con éxito");
		bancoResponse.setSuccess(true);

		// Mock de UserResponse
		UserResponse userResponse = new UserResponse("test@ejemplo.com", "Test", "User", "12/12/2012");

		// Mock de EventResponse
		EventResponse mockEventResponse = new EventResponse();
		mockEventResponse.setPrecioMinimo(BigDecimal.valueOf(50));
		mockEventResponse.setPrecioMaximo(BigDecimal.valueOf(150));

		// Configuración de los mocks
		when(bancoClient.autenticarUsuario(anyMap())).thenReturn(Map.of("token", token));
		when(userClient.getUserByEmail("test@ejemplo.com")).thenReturn(userResponse);
		when(eventClient.obtenerDetallesEvento(eq(eventId))).thenReturn(mockEventResponse);
		when(bancoClient.validarCompra(eq(bancoRequest), eq("Bearer " + token))).thenReturn(bancoResponse);

		// Mock del repositorio para verificar que se guarda la compra
		when(compraRepository.save(ArgumentMatchers.any())).thenAnswer(invocation -> {
			Compra compra = invocation.getArgument(0);
			compra.setIdEvent(UUID.randomUUID()); // Simula que la base de datos asigna un ID
			return compra;
		});

		// Llamada al método del servicio
		CompraResponse response = compraService.registrarCompra(compraRequest);

		// Verifica la respuesta
		assertNotNull(response);
		assertTrue(response.isSuccess());
		assertEquals("Compra realizada con éxito", response.getMessage());
		assertEquals("123456", response.getTransactionId());
		assertNotNull(response.getAmount());
		assertTrue(response.getAmount().compareTo(mockEventResponse.getPrecioMinimo()) >= 0);
		assertTrue(response.getAmount().compareTo(mockEventResponse.getPrecioMaximo()) <= 0);

		// Verifica interacciones
		verify(bancoClient).autenticarUsuario(anyMap());
		verify(userClient).getUserByEmail("test@ejemplo.com");
		verify(eventClient).obtenerDetallesEvento(eq(eventId));
		verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
		verify(compraRepository).save(ArgumentMatchers.any(Compra.class));
	}

}
