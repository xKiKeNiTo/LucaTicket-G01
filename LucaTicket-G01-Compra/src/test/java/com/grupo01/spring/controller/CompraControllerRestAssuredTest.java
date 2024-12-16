package com.grupo01.spring.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompraControllerRestAssuredTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/compras";
    }

    @Test
    public void debeManejarErrorCuandoBancoDevuelveTransaccionFallida() {

        //Envio un numero de tarjeta fuera de limites
        String nuevaCompra = """
                {
                  "email": "kike.verac@gmail.com",
                  "eventId": "2b6f46b1-66f7-4157-9831-6c10551e2116",
                  "bancoRequest": {
                    "nombreTitular": "Enrique Vera",
                    "numeroTarjeta": "41111111111111110",
                    "mesCaducidad": "12",
                    "yearCaducidad": "2025",
                    "cvv": "123",
                    "emisor": "Visa",
                    "concepto": "Compra de entradas",
                    "cantidad": 50.0
                  }
                }
				""";

        // Ejecuto la respuesta
        Response response = given()
                .contentType("application/json")
                .body(nuevaCompra)
                .when()
                .post("/save");

        // Loggeo la respuesta
        response.then().log().body();

        // Verifico la respuesta
        response.then()
                .statusCode(400) // Expected error status
                .body("errors[0].message", startsWith("Error en la solicitud al servicio externo"));

    }

    @Test
    public void debeDevolver400CuandoDatosDeCompraSonInvalidos() {

        //Envio un id de evento inexistente
        String nuevaCompra = """
                {
                  "email": "kike.verac@gmail.com",
                  "eventId": "2b6f46b1-66f7-4157-9831-6c10551e2116",
                  "bancoRequest": {
                    "nombreTitular": "Enrique Vera",
                    "numeroTarjeta": "41111111111111110",
                    "mesCaducidad": "12",
                    "yearCaducidad": "2025",
                    "cvv": "1234",
                    "emisor": "Visa",
                    "concepto": "Compra de entradas",
                    "cantidad": 50.0
                  }
                }
				""";

        // Ejecuto la respuesta
        Response response = given()
                .contentType("application/json")
                .body(nuevaCompra)
                .when()
                .post("/save");

        // Loggeo la respuesta
        response.then().log().body();

        // Verifico la respuesta
        response.then()
                .statusCode(400); // Expected error status
    }
}