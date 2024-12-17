package com.grupo01.spring.controller;

import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
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

	@Test
	void debeRegistrarCompraYDevolver201() {
		// Arrange: Crear los datos del BancoRequest
		BancoRequest bancoRequest = new BancoRequest();
		bancoRequest.setNombreTitular("Juan Perez");
		bancoRequest.setNumeroTarjeta("1234567812345678");
		bancoRequest.setMesCaducidad("12");
		bancoRequest.setYearCaducidad("2025");
		bancoRequest.setCvv("123");
		bancoRequest.setEmisor("Visa");
		bancoRequest.setConcepto("Compra de entradas");
		bancoRequest.setCantidad(new BigDecimal("150.00"));

		// Crear el CompraRequest
		CompraRequest compraRequest = new CompraRequest();
		compraRequest.setEmail("usuario@example.com");
		compraRequest.setEventId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
		compraRequest.setBancoRequest(bancoRequest);

		// Act & Assert: Enviar la solicitud POST al endpoint /compras
		given().port(port).contentType(ContentType.JSON).body(compraRequest).when().post("/compras").then()
				.statusCode(201) // Verificar que responde con HTTP 201 Created
				.contentType(ContentType.JSON) // Verificar el tipo de contenido
				.body("message", equalTo("Compra realizada con éxito")) // Mensaje de éxito
				.body("success", is(true)) // Campo "success" verdadero
				.body("transactionId", notNullValue()) // transactionId no debe ser nulo
				.body("amount", equalTo(bancoRequest.getCantidad().floatValue())); // Verificar el monto
	}

	@Test
	public void debeManejarErrorCuandoBancoDevuelveTransaccionFallida() {

		// Envio un numero de tarjeta fuera de limites
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
		Response response = given().contentType("application/json").body(nuevaCompra).when().post("/save");

		// Loggeo la respuesta
		response.then().log().body();

		// Verifico la respuesta
		response.then().statusCode(400) // Expected error status
				.body("errors[0].message", startsWith("Error en la solicitud al servicio externo"));

	}

	@Test
	public void debeDevolver400CuandoDatosDeCompraSonInvalidos() {

		// Envio un cvv no valido
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
		Response response = given().contentType("application/json").body(nuevaCompra).when().post("/save");

		// Loggeo la respuesta
		response.then().log().body();

		// Verifico la respuesta
		response.then().statusCode(400); // Expected error status
	}
}
