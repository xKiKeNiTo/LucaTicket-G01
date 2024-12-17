package com.grupo01.spring.service;

import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.feignClient.EventClient;
import com.grupo01.spring.feignClient.UserClient;
import com.grupo01.spring.model.*;
import com.grupo01.spring.repository.CompraRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CompraServiceImpl implements CompraService {

	private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

	private final BancoClient bancoClient;
	private final CompraRepository compraRepository;
	private final UserClient userClient;
	private final EventClient eventClient;

	public CompraServiceImpl(BancoClient bancoClient, CompraRepository compraRepository, UserClient userClient,
			EventClient eventClient) {
		this.bancoClient = bancoClient;
		this.compraRepository = compraRepository;
		this.userClient = userClient;
		this.eventClient = eventClient;
	}

	@Override
	public CompraResponse registrarCompra(CompraRequest compraRequest) {
		String token = validarUser();

		// Validar usuario
		logger.info("Consultando usuario: {}", compraRequest.getEmail());
		UserResponse userResponse;
		try {
			userResponse = userClient.getUserByEmail(compraRequest.getEmail());
		} catch (Exception e) {
			throw createError(HttpStatus.NOT_FOUND, "Email no encontrado", "El usuario no existe.", compraRequest);
		}

		// Validar evento
		EventResponse eventResponse;
		try {
			eventResponse = eventClient.obtenerDetallesEvento(compraRequest.getEventId());
		} catch (Exception e) {
			throw createError(HttpStatus.NOT_FOUND, "Evento no encontrado", "El evento no existe.", compraRequest);
		}

		// Verificar si el usuario ya compró el evento
		if (compraRepository.existsByUserMailAndIdEvent(userResponse.getMail(), compraRequest.getEventId())) {
			throw createError(HttpStatus.BAD_REQUEST, "Compra duplicada", "El usuario ya ha comprado este evento.",
					compraRequest);
		}

		// Precio aleatorio del evento
		BigDecimal ticketPrice = generateRandomPrice(eventResponse.getPrecioMinimo(), eventResponse.getPrecioMaximo());

		// Validar compra con el banco
		compraRequest.getBancoRequest().setCantidad(ticketPrice);
		BancoResponse bancoResponse = validarCompra(compraRequest.getBancoRequest(), token);

		// Guardar en base de datos
		Compra compra = new Compra();
		compra.setIdEvent(compraRequest.getEventId());
		compra.setUserMail(userResponse.getMail());
		compra.setPrecio(ticketPrice);
		compra.setFechaCompra(LocalDateTime.now());
		compraRepository.save(compra);

		return new CompraResponse("Compra realizada con éxito", true, bancoResponse.getCodigo(), ticketPrice);
	}

	private BigDecimal generateRandomPrice(BigDecimal min, BigDecimal max) {
		Random random = new Random();
		BigDecimal randomValue = min.add(BigDecimal.valueOf(random.nextDouble()).multiply(max.subtract(min)));
		return randomValue.setScale(2, RoundingMode.HALF_UP);
	}

	private String validarUser() {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("user", "Grupo01");
		credentials.put("password", "AntoniosRules");
		return bancoClient.autenticarUsuario(credentials).get("token");
	}

	private BancoResponse validarCompra(BancoRequest bancoRequest, String token) {
		return bancoClient.validarCompra(bancoRequest, "Bearer " + token);
	}

	private ResponseStatusException createError(HttpStatus status, String error, String message,
			CompraRequest request) {
		Map<String, Object> errorBody = new HashMap<>();
		errorBody.put("timestampp", LocalDateTime.now());
		errorBody.put("status", status.value());
		errorBody.put("error", error);
		errorBody.put("message", List.of(message));
		errorBody.put("info", request.getBancoRequest());
		errorBody.put("infoadicional", "Error en el procesamiento de la solicitud.");
		return new ResponseStatusException(status, message);
	}
}
