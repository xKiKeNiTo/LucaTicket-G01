package com.grupo01.spring.service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		// Validar usuario
		String token = validarUser();

		// Recibir datos del usuario
		logger.info("Consultando datos del usuario: {}", compraRequest.getEmail());
		UserResponse userResponse = userClient.getUserByEmail(compraRequest.getEmail());

		logger.info("Usuario obtenido: {}", userResponse);

		// Recibir datos del evento
		logger.info("Consultando datos del evento: {}", compraRequest.getEventId());
		EventResponse eventResponse = eventClient.obtenerDetallesEvento(compraRequest.getEventId());

		logger.info("Evento obtenido: {}", eventResponse);

		// Generar número random entre los valores minimo y máximo
		BigDecimal ticketPrice = generateRandomPrice(eventResponse.getPrecioMinimo(), eventResponse.getPrecioMaximo());
		logger.info("Precio generado aleatoriamente para la entrada: {}", ticketPrice);

		// Validar compra
		compraRequest.getBancoRequest().setCantidad(ticketPrice); // Set ticket price
		BancoResponse bancoResponse = validarCompra(compraRequest.getBancoRequest(), token);

		// Guardar en la BBDD
		Compra compra = new Compra();
		compra.setIdEvent(compraRequest.getEventId());
		compra.setUserMail(userResponse.getMail());
		compra.setPrecio(ticketPrice);
		compra.setFechaCompra(LocalDateTime.now());
		compraRepository.save(compra);

		// Devolver respuesta
		logger.info("Compra validada y procesada exitosamente. Transaction ID: {}", bancoResponse.getCodigo());

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

		logger.info("Enviando solicitud de autenticación al banco con credenciales: {}", credentials);
		Map<String, String> authResponse = bancoClient.autenticarUsuario(credentials);
		logger.info("Respuesta recibida del banco: {}", authResponse);

		String token = authResponse.get("token");
		logger.info("Token recibido correctamente: {}", token);

		return token;
	}

	private BancoResponse validarCompra(BancoRequest bancoRequest, String token) {
		logger.info("Enviando solicitud de validación de compra con token: {}", token);
		logger.info("Datos enviados en la solicitud: {}", bancoRequest);

		BancoResponse bancoResponse = bancoClient.validarCompra(bancoRequest, "Bearer " + token);
		logger.info("Respuesta de validación del banco: {}", bancoResponse);

		return bancoResponse;
	}
}
