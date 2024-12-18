package com.grupo01.spring.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import com.grupo01.spring.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.feignClient.EventClient;
import com.grupo01.spring.feignClient.UserClient;
import com.grupo01.spring.repository.CompraRepository;

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

        // Verifica el mensaje de la excepción
        assertEquals("El banco rechazó la compra", exception.getMessage());

        // Verifica que no se haya guardado en la base de datos
        verify(compraRepository, never()).save(ArgumentMatchers.any(Compra.class));

        // Verificar interacciones
        verify(bancoClient).autenticarUsuario(anyMap());
        verify(userClient).getUserByEmail("test@ejemplo.com");
        verify(eventClient).obtenerDetallesEvento(compraRequest.getEventId());
        verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
    }

    @Test
    void debeRegistrarCompraCorrectamente() {
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
            compra.setIdEvent(UUID.randomUUID());
            return compra;
        });

        // Llamada al método del servicio
        CompraResponse response = compraService.registrarCompra(compraRequest);

        // Verifica la respuesta
        assertNotNull(response);
        assertEquals("Compra realizada con éxito", response.getMessage()[0]);
        assertNotNull(response.getInfo());

        // Verifica interacciones
        verify(bancoClient).autenticarUsuario(anyMap());
        verify(userClient).getUserByEmail("test@ejemplo.com");
        verify(eventClient).obtenerDetallesEvento(eq(eventId));
        verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
        verify(compraRepository).save(ArgumentMatchers.any(Compra.class));
    }

    @Test
    void debeCalcularPrecioPromedioCorrectamente() {
        // Datos de prueba
        UUID eventId = UUID.randomUUID();
        BigDecimal expectedPrecioPromedio = BigDecimal.valueOf(100); // Precio promedio esperado

        when(compraRepository.calcularPrecioPromedioPorEvento(eventId)).thenReturn(expectedPrecioPromedio);

        BigDecimal precioPromedio = compraService.calcularPrecioPromedioPorEvento(eventId);
        assertEquals(expectedPrecioPromedio, precioPromedio);

        verify(compraRepository).calcularPrecioPromedioPorEvento(eq(eventId));
    }
}
