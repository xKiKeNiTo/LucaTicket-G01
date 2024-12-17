package com.grupo01.spring.feignClient.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BancoErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(BancoErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = extractResponseBody(response);

        logger.error("Error recibido del servicio {}: {}", methodKey, responseBody);

        switch (response.status()) {
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, extractMessage(responseBody, "Recurso no encontrado"));
            case 400:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, extractMessage(responseBody, "Solicitud inválida"));
            case 500:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, extractMessage(responseBody, "Error interno del servidor"));
            default:
                return new ResponseStatusException(HttpStatus.valueOf(response.status()), extractMessage(responseBody, "Error desconocido"));
        }
    }

    private String extractResponseBody(Response response) {
        try {
            if (response.body() != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()));
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            logger.error("Error al procesar el cuerpo de la respuesta: {}", e.getMessage());
        }
        return "No se pudo procesar el mensaje del error.";
    }

    private String extractMessage(String responseBody, String defaultMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode messageNode = root.path("message");

            if (messageNode.isTextual()) {
                return messageNode.asText();
            } else if (messageNode.isArray()) {
                return StreamSupport.stream(messageNode.spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(Collectors.joining(" "));
            }
        } catch (Exception e) {
            logger.error("Error al extraer el mensaje del cuerpo de la respuesta: {}", e.getMessage());
        }
        return defaultMessage;
    }
}
