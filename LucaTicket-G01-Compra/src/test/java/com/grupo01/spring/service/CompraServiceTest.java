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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.feignClient.UserClient;
import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.BancoResponse;
import com.grupo01.spring.model.Compra;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.CompraRepository;

@ExtendWith(MockitoExtension.class)
public class CompraServiceTest {
	
	@Mock
	private CompraRepository compraRepository;
	
	@Mock
	private BancoClient bancoClient;
	
	@Mock
	private UserClient userClient;
	
	@InjectMocks
	private CompraServiceImpl compraService;

	@Test
	void debeManejarErrorCuandoCompraNoEsValida() {
		 // Datos de prueba
        CompraRequest compraRequest = new CompraRequest();
        compraRequest.setEmail("test@ejemplo.com");
        compraRequest.setEventId(UUID.randomUUID());
        
        BancoRequest bancoRequest = new BancoRequest();
        bancoRequest.setNumeroTarjeta("1234-5678-1234-5678");
        bancoRequest.setCvv("123");
        bancoRequest.setCantidad(BigDecimal.valueOf(100));
        compraRequest.setBancoRequest(bancoRequest);

        String token = "mocked-token";
        
        // Mock del flujo de datos
        when(bancoClient.autenticarUsuario(anyMap())).thenReturn(Map.of("token", token));
        when(userClient.getUserByEmail("test@ejemplo.com")).thenReturn(new UserResponse("12345", "Test User", "test@ejemplo.com"));

        // Simula respuesta inválida del banco
        when(bancoClient.validarCompra(eq(bancoRequest), eq("Bearer " + token)))
        	.thenThrow(new RuntimeException("El banco rechazó la compra"));
        
        // Verifica comportamiento esperado
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            compraService.registrarCompra(compraRequest);
        });
        
        // Verificar el mensaje de la excepción
        assertEquals("Error al validar la compra: El banco rechazó la compra", exception.getMessage());

        // Verificar interacciones
        verify(bancoClient).autenticarUsuario(anyMap());
        verify(userClient).getUserByEmail("test@ejemplo.com");
        verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));
        verifyNoInteractions(compraRepository); // No debe guardar en la base de datos si hay error
    }
	
	@Test
	void debeConsumirMicroservicioBancoCorrectamente() {
		// Datos de prueba
		CompraRequest compraRequest = new CompraRequest();
	    compraRequest.setEmail("test@ejemplo.com");
	    compraRequest.setEventId(UUID.randomUUID());
	            
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
	    
	    UserResponse userResponse = new UserResponse("12345", "Test User", "test@ejemplo.com");

	    // Mock del flujo de datos
	    when(bancoClient.autenticarUsuario(anyMap())).thenReturn(Map.of("token", token));
	    when(userClient.getUserByEmail("test@ejemplo.com")).thenReturn(userResponse);
	    when(bancoClient.validarCompra(eq(bancoRequest), eq("Bearer " + token))).thenReturn(bancoResponse);

	    // Llama al método del servicio
	    CompraResponse response = compraService.registrarCompra(compraRequest);

	    // Verifica el resultado
	    assertNotNull(response);
	    assertTrue(response.isSuccess());
	    assertEquals("Compra realizada con éxito", response.getMessage());
	    assertEquals("123456", response.getTransactionId());
	    assertEquals(BigDecimal.valueOf(100), response.getAmount());
	    	    
	    // Verifica interacciones
	    verify(bancoClient).autenticarUsuario(anyMap());
	    verify(userClient).getUserByEmail("test@ejemplo.com");
	    verify(bancoClient).validarCompra(eq(bancoRequest), eq("Bearer " + token));	    
	}
                		
}
