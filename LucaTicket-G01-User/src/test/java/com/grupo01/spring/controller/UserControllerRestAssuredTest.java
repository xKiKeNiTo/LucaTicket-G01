package com.grupo01.spring.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
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

//    @Test
//    public void debeCrearUsuarioCorrectamente() {
//        // JSON de prueba para un usuario nuevo
//        String nuevoUsuarioJson = """
//                {
//                    "mail": "test@example.com",
//                    "nombre": "NombreTest",
//                    "apellido": "ApellidoTest",
//                    "contrasena": "password123",
//                    "fechaAlta": "2024-12-12"
//                }
//                """;
//
//        // Enviar POST y validar la respuesta
//        given()
//            .contentType("application/json")
//            .body(nuevoUsuarioJson)
//        .when()
//            .post("/users/save")
//        .then()
//            .statusCode(201) // Verifica que el estado sea 201 Created
//            .body("mail", equalTo("test@example.com"))
//            .body("nombre", equalTo("NombreTest"))
//            .body("apellido", equalTo("ApellidoTest"))
//            .body("fechaAlta", equalTo("2024-12-12"));
//    }
    
//    @Test
//    public void debeDevolver400CuandoDatosDeUsuarioSonInvalidos() {
//        // JSON con datos inválidos
//        String usuarioInvalidoJson = """
//            {
//              "mail": "correo-invalido",
//              "nombre": "",
//              "apellido": "",
//              "contrasena": "123",
//              "fechaAlta": null
//            }
//        """;
//
//        given()
//            .contentType(ContentType.JSON)
//            .body(usuarioInvalidoJson)
//        .when()
//            .post("/users/save")
//        .then()
//            .statusCode(400) // Verifica que se devuelve HTTP 400
//            .body("message", notNullValue()) // Comprueba que hay un mensaje de error en la respuesta
//            .body("message", containsString("El correo electrónico debe ser válido")); // Mensaje de validación esperado
//    }
    
}
