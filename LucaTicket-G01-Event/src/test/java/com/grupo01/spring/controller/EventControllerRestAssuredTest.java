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
		when().get("/all").then().statusCode(200).body("totalEventos", greaterThan(0)) // Validación del total de
																						// eventos
				.body("eventos.size()", greaterThan(0)) // Validación del tamaño de la lista de eventos
				.body("eventos[0].nombre", notNullValue()); // Validación de un campo específico dentro de la lista
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

		given().contentType("application/json").body(nuevoEventoJson).when().post("/save").then().statusCode(201)
				.body("nombre", equalTo("Concierto de Jazz")).body("localidad", equalTo("Barcelona"));

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

		given().contentType("application/json").body(eventoIncompleto).when().post("/save").then().statusCode(400)
				.body("errors.size()", greaterThan(0)) // Validación del tamaño de los errores
				.body("errors.message", hasItems("El nombre del evento no puede estar vacío",
						"La fecha del evento no puede estar vacía"));
	}

	@Test
	public void debeDevolverEventosPorNombre() {
		given().queryParam("nombre", "Concierto de Jazz") // Enviamos el nombre como parámetro de consulta
				.when().get("/nombre") // Endpoint para buscar eventos por nombre
				.then().statusCode(200) // Verificamos que el estado sea 200
				.body("totalEventos", greaterThan(0)) // Comprobamos que `totalEventos` sea mayor que 0
				.body("eventos", notNullValue()) // Verificamos que la lista `eventos` no sea nula
				.body("eventos[0].nombre", containsString("Concierto de Jazz")); // Verificamos que al menos un evento contenga
																	// "Concierto de Jazz" en el nombre
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
				.statusCode(200)
				.body(org.hamcrest.Matchers.equalTo(detalles));
	}

	@Test
    public void debeModificarEvento() {
        String id = "28fb994c-2ef1-4045-8f6c-49610e824b65";

        // JSON de evento actualizado
        String eventoActualizadoJson = """
                {
                    "nombre": "Concierto Modificado",
                    "descripcion": "Descripción actualizada del evento",
                    "fechaEvento": "2024-12-20",
                    "horaEvento": "19:00:00",
                    "precioMinimo": 70.00,
                    "precioMaximo": 150.00,
                    "localidad": "Sevilla",
                    "nombreRecinto": "Auditorio Sevilla",
                    "generoMusica": "Rock"
                }
                """;

        given()
            .contentType("application/json")
            .queryParam("id", id)
            .body(eventoActualizadoJson)
        .when()
            .put("/edit")
        .then()
            .statusCode(200)
            .body("message", equalTo("Evento modificado correctamente"))
            .body("data.id", equalTo(id))
            .body("data.nombre", equalTo("Concierto Modificado"))
            .body("data.descripcion", equalTo("Descripción actualizada del evento"))
            .body("data.localidad", equalTo("Sevilla"))
            .body("data.precioMinimo", equalTo(70.00f))
            .body("data.precioMaximo", equalTo(150.00f));
    }

}
