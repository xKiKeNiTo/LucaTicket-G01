package com.grupo01.spring.controller;

import com.grupo01.spring.model.CompraResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompraControllerRestAssuredTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		RestAssured.basePath = "/users";
	}

	@Test
	void debeListarComprasPorCorreoCorrectamente() {
		// Arrange: Correo y datos simulados
		String correo = "usuario@example.com";

		CompraResponse compraResponse1 = new CompraResponse("Compra encontrada exitosamente", true,
				UUID.randomUUID().toString(), BigDecimal.valueOf(150.00));
		CompraResponse compraResponse2 = new CompraResponse("Compra encontrada exitosamente", true,
				UUID.randomUUID().toString(), BigDecimal.valueOf(200.00));

		// Mock del comportamiento esperado en el backend
		// Supongamos que utilizamos un controlador simulado o una base de datos en
		// memoria para este ejemplo.
		given().port(port).contentType(ContentType.JSON).body(new CompraResponse[] { compraResponse1, compraResponse2 })
				.when().post("/mock/compras") // Crea un endpoint mock si es necesario.
				.then().statusCode(201);

		// Act: Realiza la solicitud GET con el correo como parámetro
		Response response = given().port(port).queryParam("mail", correo).when().get("/compras").then().statusCode(200) // Assert:
																														// Verifica
																														// el
																														// código
																														// de
																														// respuesta
																														// HTTP
				.contentType(ContentType.JSON) // Assert: Verifica el tipo de contenido
				.body("$.size()", is(2)) // Verifica que se obtienen 2 compras
				.body("[0].transactionId", notNullValue())
				.body("[0].amount", equalTo(compraResponse1.getAmount().floatValue()))
				.body("[1].transactionId", notNullValue())
				.body("[1].amount", equalTo(compraResponse2.getAmount().floatValue())).extract().response();

		// Assert: Imprimir la respuesta para inspección (opcional, útil para debugging)
		System.out.println(response.asPrettyString());
	}

}
