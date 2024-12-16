package com.grupo01.spring.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerRestAssuredTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/users";
    }

    @Test
    public void debeCrearUsuarioCorrectamente() {
        // JSON de prueba para un usuario nuevo
        String nuevoUsuarioJson = """
                {
                    "mail": "test@example.com",
                    "nombre": "NombreTest",
                    "apellido": "ApellidoTest",
                    "contrasena": "password123",
                    "fechaAlta": "2024-12-12"
                }
                """;

        // Enviar POST y validar la respuesta
        given()
            .contentType("application/json")
            .body(nuevoUsuarioJson)
        .when()
            .post("/save")
        .then()
            .statusCode(201) // Verifica que el estado sea 201 Created
            .body("mail", equalTo("test@example.com"))
            .body("nombre", equalTo("NombreTest"))
            .body("apellido", equalTo("ApellidoTest"))
            .body("fechaAlta", equalTo("2024-12-12"));
    }
}
