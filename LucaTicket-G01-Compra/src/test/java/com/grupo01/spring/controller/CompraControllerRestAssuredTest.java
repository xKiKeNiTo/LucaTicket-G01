package com.grupo01.spring.controller;

import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.CompraRequest;
import com.grupo01.spring.model.CompraResponse;
import com.grupo01.spring.service.CompraService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompraControllerRestAssuredTest {

	@LocalServerPort
	private int port;

	@MockBean
	private CompraService compraService;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void debeListarComprasPorCorreoCorrectamente() {
		// Datos simulados
		Map<String, Object> datosSimulados = new HashMap<>();
		datosSimulados.put("correo", "test@example.com");
		datosSimulados.put("compras", Arrays.asList(Map.of("producto", "Producto A", "monto", 100.00),
				Map.of("producto", "Producto B", "monto", 50.00)));

		// Configurar el mock del servicio
		when(compraService.listarComprasPorCorreo("test@example.com")).thenReturn(datosSimulados);

		// Prueba con RestAssured
		given().queryParam("mail", "test@example.com").when().get("/compras/compras").then().statusCode(200)
				.body("correo", equalTo("test@example.com")).body("compras[0].producto", equalTo("Producto A"))
				.body("compras[0].monto", equalTo(100.00f)).body("compras[1].producto", equalTo("Producto B"))
				.body("compras[1].monto", equalTo(50.00f));
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

	    CompraRequest compraRequest = new CompraRequest();
	    compraRequest.setEmail("usuario@example.com");
	    compraRequest.setEventId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
	    compraRequest.setBancoRequest(bancoRequest);

	    // Act & Assert: Enviar solicitud POST al endpoint /compras/save
	    given()
	        .port(port) 
	        .contentType(ContentType.JSON)
	        .body(compraRequest) 
	    .when()
	        .post("/compras/save") // Hacer POST al endpoint
	    .then()
	        .statusCode(201); // Verificar código de estado 201
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
		Response response = given().contentType("application/json").body(nuevaCompra).when().post("/compras/save");

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
		Response response = given().contentType("application/json").body(nuevaCompra).when().post("/compras/save");

		// Loggeo la respuesta
		response.then().log().body();

		// Verifico la respuesta
		response.then().statusCode(400); // Expected error status
	}
}