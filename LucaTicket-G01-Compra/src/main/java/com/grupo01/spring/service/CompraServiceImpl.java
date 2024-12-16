package com.grupo01.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.feignClient.UserClient;
import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.BancoResponse;
import com.grupo01.spring.model.Compra;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.CompraRepository;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompraServiceImpl implements CompraService {

	private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

	private final BancoClient bancoClient;
	private final CompraRepository compraRepository;
	private final UserClient userClient;

	public CompraServiceImpl(BancoClient bancoClient, CompraRepository compraRepository, UserClient userClient) {
		this.bancoClient = bancoClient;
		this.compraRepository = compraRepository;
		this.userClient = userClient;
	}

	@Override
	public CompraResponse registrarCompra(CompraRequest compraRequest) {
		// Paso 1: Validar Usuario
		String token = validarUser();

		// Paso 2: Obtener datos del Usuario
		logger.info("Consultando datos del usuario: {}", compraRequest.getEmail());
		UserResponse userResponse = userClient.getUserByEmail(compraRequest.getEmail());

		// Paso 3: Validar Compra
		BancoResponse bancoResponse = validarCompra(compraRequest.getBancoRequest(), token);

		// Paso 4: Guardar Compra en la Base de Datos
		Compra compra = new Compra();
		compra.setIdEvent(compraRequest.getEventId());
		compra.setUserMail(userResponse.getEmail());
		compra.setPrecio(compraRequest.getBancoRequest().getCantidad());
		compra.setFechaCompra(LocalDateTime.now());
		compraRepository.save(compra);

		// Paso 5: Retornar la respuesta de éxito
		logger.info("Compra validada y procesada exitosamente. Transaction ID: {}", bancoResponse.getCodigo());

		return new CompraResponse("Compra realizada con éxito", true, bancoResponse.getCodigo(),
				compraRequest.getBancoRequest().getCantidad());
	}

	// Método para validar al usuario y obtener el token
	private String validarUser() {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("user", "Grupo01");
		credentials.put("password", "AntoniosRules");

		try {
			logger.info("Enviando solicitud de autenticación al banco con credenciales: {}", credentials);

			Map<String, String> authResponse = bancoClient.autenticarUsuario(credentials);

			logger.info("Respuesta recibida del banco: {}", authResponse);

			String token = authResponse.get("token");

			if (token == null || token.isEmpty()) {
				logger.error("No se recibió token en la respuesta del banco.");
				throw new RuntimeException("La autenticación falló: no se recibió un token válido.");
			}

			logger.info("Token recibido correctamente: {}", token);
			return token;
		} catch (FeignException e) {
			logger.error("Error al autenticar con el banco. Código HTTP: {}, Respuesta: {}", e.status(),
					e.contentUTF8());
			throw new RuntimeException("Error al autenticar con el banco: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Excepción inesperada durante la autenticación con el banco", e);
			throw new RuntimeException("Error al autenticar con el banco: " + e.getMessage());
		}
	}

	// Método para validar la compra
	private BancoResponse validarCompra(BancoRequest bancoRequest, String token) {
		try {
			logger.info("Enviando solicitud de validación de compra con token: {}", token);
			logger.info("Datos enviados en la solicitud: {}", bancoRequest);

			BancoResponse bancoResponse = bancoClient.validarCompra(bancoRequest, "Bearer " + token);

			logger.info("Respuesta de validación del banco: {}", bancoResponse);

			if (!bancoResponse.isSuccess()) {
				logger.warn("El banco rechazó la validación: {}", bancoResponse.getMensaje());
				throw new RuntimeException("La compra no pudo ser procesada: " + bancoResponse.getMensaje());
			}

			return bancoResponse;
		} catch (FeignException e) {
			String errorMessage = formatFeignException(e);
			logger.error("Error al validar la compra: {}", errorMessage);
			throw new RuntimeException("Error al validar la compra: " + errorMessage);
		} catch (Exception e) {
			logger.error("Excepción inesperada durante la validación de la compra", e);
			throw new RuntimeException("Error al validar la compra: " + e.getMessage());
		}
	}

	// Método auxiliar para formatear errores de Feign
	private String formatFeignException(FeignException e) {
		String responseBody = e.contentUTF8(); // Captura el cuerpo de la respuesta
		if (responseBody == null || responseBody.isEmpty()) {
			return "El servidor devolvió un error sin cuerpo.";
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(responseBody);
			String status = root.path("status").asText("N/A");
			String error = root.path("error").asText("N/A");
			String message = root.path("message").isArray()
					? String.join(", ", mapper.convertValue(root.path("message"), List.class))
					: root.path("message").asText("N/A");

			return String.format("Status: %s, Error: %s, Mensaje: %s", status, error, message);
		} catch (Exception ex) {
			logger.error("No se pudo analizar la respuesta de error del servidor.", ex);
			return "Error no procesable: " + responseBody;
		}
	}
}
