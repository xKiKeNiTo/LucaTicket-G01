package com.grupo01.spring.controller;

import com.grupo01.spring.model.EventResponse;
import com.grupo01.spring.model.Localidad;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

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
		.statusCode(200)
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

	@Test
	public void debeDevolverDetallesEvento() {
		String id = "7e3b390b-671c-499b-b9c5-40f80dc2547e";

		String detalles = "El evento 'Concierto de Jazz' se realiza en Barcelona el dia 2024-06-10 a las 18:00";

		// Perform the test
		RestAssured.given()
				.queryParam("id", id)
				.when()
				.get("/detalles")
				.then()
				.statusCode(200) // Assert 200 OK
				.body(org.hamcrest.Matchers.equalTo(detalles)); // Assert body matches
	}

}
