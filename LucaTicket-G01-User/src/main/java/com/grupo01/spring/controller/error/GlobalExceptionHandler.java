package com.grupo01.spring.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase para manejar excepciones globales en la aplicación. Captura y gestiona
 * errores comunes de manera centralizada.
 *
 * @version 1.0
 * @author Raul
 * @date 02/12/2024
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private ResponseEntity<Object> buildErrorResponse(String message, int code, String field) {
		Map<String, Object> error = new HashMap<>();
		error.put("message", message);
		error.put("code", code);
		error.put("field", field);
		return new ResponseEntity<>(Map.of("errors", List.of(error)), HttpStatus.valueOf(code));
	}

	/**
	 * Captura excepciones de validación y retorna una respuesta simplificada.
	 *
	 * @param ex Excepción generada por errores de validación.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
			Map<String, Object> error = new HashMap<>();
			error.put("field", fieldError.getField());
			error.put("message", fieldError.getDefaultMessage());
			error.put("code", HttpStatus.BAD_REQUEST.value());
			return error;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Captura excepciones de deserialización de valores inválidos y retorna una
	 * respuesta simplificada.
	 *
	 * @param ex Excepción generada por valores inválidos en deserialización.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
		String fieldName = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		String rejectedValue = String.valueOf(ex.getValue());
		String validValues = "N/A";
		String message;

		// Si el tipo esperado es un Enum, obtener los valores válidos
		if (ex.getTargetType().isEnum()) {
			validValues = List.of(ex.getTargetType().getEnumConstants()).toString();
			message = String.format("El valor '%s' no es válido para el campo '%s'. Valores válidos: %s.",
					rejectedValue, fieldName, validValues);
		}
		// Si el tipo esperado es LocalTime o LocalDate, indicar el formato correcto
		else if (ex.getTargetType() == java.time.LocalTime.class) {
			validValues = "Formato esperado: HH:mm:ss (ejemplo: 14:30:00)";
			message = String.format("El valor '%s' no es válido para el campo '%s'. %s", rejectedValue, fieldName,
					validValues);
		} else if (ex.getTargetType() == java.time.LocalDate.class) {
			validValues = "Formato esperado: yyyy-MM-dd (ejemplo: 2024-12-11)";
			message = String.format("El valor '%s' no es válido para el campo '%s'. %s", rejectedValue, fieldName,
					validValues);
		}
		// Para otros tipos, mensaje genérico
		else {
			message = String.format("El valor '%s' no es válido para el campo '%s'.", rejectedValue, fieldName);
		}

		return buildErrorResponse(message, HttpStatus.BAD_REQUEST.value(), fieldName);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		// Verificar si la causa es una InvalidFormatException
		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException) {
			// Delegar el manejo al método de InvalidFormatException
			return handleInvalidFormatException((InvalidFormatException) cause);
		}

		// Manejo genérico para otros casos
		String message = "Error en el formato del JSON";
		return buildErrorResponse(message, HttpStatus.BAD_REQUEST.value(), null);
	}

	/**
	 * Manejador de excepciones personalizadas.
	 *
	 * @param ex Excepción personalizada lanzada por la lógica de negocio.
	 * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP
	 *         correspondiente.
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		return buildErrorResponse(ex.getMessage(), ex.getErrorCode(), null);
	}

	/**
	 * Manejador de excepciones para métodos HTTP no soportados.
	 *
	 * @param ex Excepción lanzada cuando se usa un método HTTP no soportado.
	 * @return Un objeto ResponseEntity con los detalles del error y el estado HTTP
	 *         405 (Method Not Allowed).
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		String message = String.format("Método HTTP '%s' no soportado. Métodos válidos: %s", ex.getMethod(),
				ex.getSupportedHttpMethods());
		return buildErrorResponse(message, HttpStatus.METHOD_NOT_ALLOWED.value(), null);
	}

	/**
	 * Captura excepciones de tipo `MethodArgumentTypeMismatchException` y retorna
	 * un mensaje simplificado.
	 *
	 * @param ex Excepción lanzada cuando un argumento no puede ser convertido.
	 * @return Respuesta con el campo, mensaje de error y código HTTP.
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex) {
		String message = "Ruta no encontrada: " + ex.getRequestURL();
		return buildErrorResponse(message, HttpStatus.NOT_FOUND.value(), null);
	}

	/**
	 * Manejo de errores cuando falta un parámetro requerido en la solicitud.
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
		String paramName = ex.getParameterName();
		String paramType = ex.getParameterType();
		String message = String.format("El parámetro requerido '%s' de tipo '%s' no está presente.", paramName,
				paramType);
		return buildErrorResponse(message, HttpStatus.BAD_REQUEST.value(), paramName);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String parameterName = ex.getName();
		String providedValue = ex.getValue() != null ? ex.getValue().toString() : "null";
		String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
		String message = String.format(
				"El parámetro '%s' con valor '%s' no es válido. Se esperaba un valor del tipo '%s'.", parameterName,
				providedValue, expectedType);

		Map<String, Object> error = new HashMap<>();
		error.put("message", message);
		error.put("code", HttpStatus.BAD_REQUEST.value());
		error.put("parameter", parameterName);

		return new ResponseEntity<>(Map.of("errors", List.of(error)), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<Object> handleDuplicateUserException(DuplicateUserException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("message", ex.getMessage());
		error.put("code", HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(Map.of("errors", List.of(error)), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
		logger.error("Error capturado: {}", ex.getReason());

		// Convertir HttpStatusCode a HttpStatus
		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", status.value());
		errorResponse.put("error", status.getReasonPhrase());
		errorResponse.put("message", List.of(ex.getReason()));
		errorResponse.put("infoadicional", "Error en el procesamiento de la solicitud.");

		return new ResponseEntity<>(Map.of("errors", List.of(errorResponse)), status);
	}

}
