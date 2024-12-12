package com.grupo01.spring.controller;

import io.restassured.RestAssured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerRestAssuredTest {
	
	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		RestAssured.basePath = "/eventos";				
	}

	@Test
	public void debeDevolverListaDeEventosCorrectamente() {
		when().get("/all").then().statusCode(200).body("$", hasSize(greaterThan(0))).body("[0].nombre", notNullValue());
	}

	@Test
	public void debeCrearEvento() {
		String nuevoEventoJson = """
				{
				    "nombre": "Concierto de Jazz",
				    "descripcion": "Un evento musical al aire libre",
				    "fechaEvento": "2024-06-10",
				    "horaEvento": "18:00:00",
				    "precioMinimo": 30.00,
				    "precioMaximo": 100.00,
				    "localidad": "Barcelona",
				    "nombreRecinto": "Parque de la Música",
				    "generoMusica": "Jazz"
				}
				""";

		given()
		.contentType("application/json")
		.body(nuevoEventoJson)
		.when()
		.post("/save")
		.then()
		.statusCode(201)
		.body("nombre", equalTo("Concierto de Jazz"))
		.body("localidad", equalTo("Barcelona"));
	}
	
	@Test
	public void debeDevolverErrorCuandoFaltanCampos() {
	    String eventoIncompleto = """
	        {
	            "nombre": "",
	            "fechaEvento": null,
	            "horaEvento": null,
	            "precioMinimo": null,
	            "precioMaximo": null,
	            "localidad": null
	        }
	        """;

	    given()
	        .contentType("application/json")
	        .body(eventoIncompleto)
	        .when()
	        .post("/save")
	        .then()
	        .statusCode(400)
	        .body("errors", hasSize(greaterThan(0)))
	        .body("errors.message", hasItems(
	            "El nombre del evento no puede estar vacío",
	            "La fecha del evento no puede estar vacía"
	        ));
	}
	
}
