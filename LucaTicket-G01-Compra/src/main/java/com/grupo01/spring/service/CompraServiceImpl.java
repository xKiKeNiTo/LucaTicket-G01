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
		logger.info("Consultando datos del usuario con email: {}", compraRequest.getEmail());
		UserResponse userResponse;
		try {
			userResponse = userClient.getUserByEmail(compraRequest.getEmail());
			logger.info("Datos del usuario obtenidos: {}", userResponse);
		} catch (ResponseStatusException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Usuario no encontrado con email: " + compraRequest.getEmail());
			}
			throw ex;
		}

		// Validar evento
		logger.info("Consultando datos del evento con ID: {}", compraRequest.getEventId());
		EventResponse eventResponse;
		try {
			eventResponse = eventClient.obtenerDetallesEvento(compraRequest.getEventId());
			logger.info("Detalles del evento obtenidos: {}", eventResponse);
		} catch (ResponseStatusException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Evento no encontrado con ID: " + compraRequest.getEventId());
			}
			throw ex;
		}

		// Verificar si ya existe una compra para este usuario y evento
		boolean compraExistente = compraRepository.existsByUserMailAndIdEvent(userResponse.getMail(),
				compraRequest.getEventId());
		if (compraExistente) {
			logger.warn("El usuario {} ya ha comprado el evento {}", userResponse.getMail(),
					compraRequest.getEventId());
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya ha comprado este evento.");
		}

		// Generar precio aleatorio
		BigDecimal ticketPrice = generateRandomPrice(eventResponse.getPrecioMinimo(), eventResponse.getPrecioMaximo());
		logger.info("Precio generado aleatoriamente para el evento: {}", ticketPrice);

		// Configurar valores en BancoRequest
		BancoRequest bancoRequest = compraRequest.getBancoRequest();
		bancoRequest.setCantidad(ticketPrice);

		// Actualizar concepto con el nombre del evento
		String emisor = "LucaTic G01";
		logger.info("Respuesta completa del evento: {}", eventResponse);
		String concepto = "Compra de entradas para el evento: " + eventResponse.getNombreEvento();
		logger.info("Concepto generado para la compra: {}", concepto);

		// Validar compra con el banco
		BancoResponse bancoResponse = validarCompra(bancoRequest, token);
		logger.info("Validación del banco exitosa: {}", bancoResponse);

		// Crear y guardar la compra
		Compra compra = new Compra();
		compra.setIdEvent(compraRequest.getEventId());
		compra.setUserMail(userResponse.getMail());
		compra.setPrecio(ticketPrice);
		compra.setFechaCompra(LocalDateTime.now());
		compraRepository.save(compra);
		logger.info("Compra guardada en la base de datos: {}", compra);

		// Crear el objeto "info" para la respuesta
		CompraResponse.Info info = new CompraResponse.Info(userResponse.getNombre(), bancoRequest.getNumeroTarjeta(),
				bancoRequest.getMesCaducidad(), bancoRequest.getYearCaducidad(), bancoRequest.getCvv(), emisor,
				concepto, ticketPrice);

		logger.info("Creación de la respuesta de compra exitosa.");
		return new CompraResponse("201", null, new String[] { "Compra realizada con éxito" }, info,
				"Transacción completada correctamente.");
	}

	@Override
	public Map<String, Object> listarComprasPorCorreo(String mail) {
		// Obtener todas las compras del repositorio
		List<Compra> compras = compraRepository.findAllByUserMail(mail);

		// Verificar si la lista está vacía
		if (compras.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se encontraron compras para el correo: " + mail);
		}

		// Crear la lista de compras con concepto detallado y cantidad
		List<Map<String, Object>> detalleCompras = compras.stream().map(compra -> {
			Map<String, Object> detalle = new LinkedHashMap<>();
			try {
				// Obtener los detalles del evento utilizando EventClient
				EventResponse eventResponse = eventClient.obtenerDetallesEvento(compra.getIdEvent());
				String concepto = String.format("Compra de entradas para el evento: %s en %s, %s",
						eventResponse.getNombreEvento(), eventResponse.getLocalidad(),
						eventResponse.getNombreRecinto());
				detalle.put("concepto", concepto);
			} catch (Exception e) {
				// Si falla, asignar un valor por defecto
				detalle.put("concepto", "Compra de entradas para el evento: Desconocido");
			}
			detalle.put("cantidad", compra.getPrecio());
			return detalle;
		}).toList();

		Map<String, Object> respuesta = new LinkedHashMap<>();
		respuesta.put("mailComprador", mail);
		respuesta.put("totalCompras", compras.size());
		respuesta.put("compras", detalleCompras);

		return respuesta;
	}

	private BigDecimal generateRandomPrice(BigDecimal min, BigDecimal max) {
		Random random = new Random();
		BigDecimal randomValue = min.add(BigDecimal.valueOf(random.nextDouble()).multiply(max.subtract(min)));
		return randomValue.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal calcularPrecioPromedioPorEvento(UUID idEvent) {
		return compraRepository.calcularPrecioPromedioPorEvento(idEvent);
	}

	private String validarUser() {
		Map<String, String> credentials = new HashMap<>();
		credentials.put("user", "Grupo01");
		credentials.put("password", "AntoniosRules");
		return bancoClient.autenticarUsuario(credentials).get("token");
	}

	private BancoResponse validarCompra(BancoRequest bancoRequest, String token) {
		try {
			logger.info("Validando compra con el banco...");
			BancoResponse bancoResponse = bancoClient.validarCompra(bancoRequest, "Bearer " + token);
			logger.info("Respuesta del banco recibida: {}", bancoResponse);
			return bancoResponse;
		} catch (ResponseStatusException ex) {
			throw ex;
		}
	}

}
