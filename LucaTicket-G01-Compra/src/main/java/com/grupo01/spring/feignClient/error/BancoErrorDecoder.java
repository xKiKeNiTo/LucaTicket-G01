package com.grupo01.spring.feignClient.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BancoErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(BancoErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = extractResponseBody(response);

        logger.error("Error recibido del servicio: {}", responseBody);

        switch (response.status()) {
            case 400:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, responseBody);
            case 500:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
            default:
                return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Error inesperado");
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
}
