package com.grupo01.spring.service;

import com.grupo01.spring.feignClient.BancoClient;
import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.BancoResponse;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompraServiceImpl implements CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraServiceImpl.class);

    @Autowired
    private BancoClient bancoClient;

    @Override
    public CompraResponse registrarCompra(CompraRequest compraRequest) {
        // Paso 1: Autenticación con el banco
        Map<String, String> credentials = new HashMap<>();
        credentials.put("user", "Grupo01");
        credentials.put("password", "AntoniosRules");

        String token;
        try {
            logger.info("Enviando solicitud de autenticación al banco con credenciales: {}", credentials);

            Map<String, String> authResponse = bancoClient.autenticarUsuario(credentials);

            logger.info("Respuesta recibida del banco: {}", authResponse);

            token = authResponse.get("token");

            if (token == null || token.isEmpty()) {
                logger.error("No se recibió token en la respuesta del banco.");
                throw new RuntimeException("La autenticación falló: no se recibió un token válido.");
            }

            logger.info("Token recibido correctamente: {}", token);
        } catch (FeignException e) {
            logger.error("Error al autenticar con el banco. Código HTTP: {}, Respuesta: {}", e.status(), e.contentUTF8());
            throw new RuntimeException("Error al autenticar con el banco: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Excepción inesperada durante la autenticación con el banco", e);
            throw new RuntimeException("Error al autenticar con el banco: " + e.getMessage());
        }

        // Paso 2: Validación de la compra con el token obtenido
        BancoRequest bancoRequest = compraRequest.getBancoRequest();
        BancoResponse bancoResponse;
        try {
            logger.info("Enviando solicitud de validación de compra con token: {}", token);
            logger.info("Datos enviados en la solicitud: {}", bancoRequest);

            bancoResponse = bancoClient.validarCompra(bancoRequest, "Bearer " + token);

            logger.info("Respuesta de validación del banco: {}", bancoResponse);
        } catch (FeignException e) {
            logger.error("Error al validar la compra. Código HTTP: {}, Respuesta: {}", e.status(), e.contentUTF8());
            throw new RuntimeException("Error al validar la compra: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Excepción inesperada durante la validación de la compra", e);
            throw new RuntimeException("Error al validar la compra: " + e.getMessage());
        }

        if (!bancoResponse.isSuccess()) {
            logger.warn("El banco rechazó la validación: {}", bancoResponse.getMensaje());
            throw new RuntimeException("La compra no pudo ser procesada: " + bancoResponse.getMensaje());
        }

        // Paso 3: Retornar la respuesta de éxito
        logger.info("Compra validada y procesada exitosamente. Transaction ID: {}", bancoResponse.getCodigo());

        return new CompraResponse("Compra realizada con éxito", true, bancoResponse.getCodigo(), bancoRequest.getCantidad());
    }
}
