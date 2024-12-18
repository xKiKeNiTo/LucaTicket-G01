package com.grupo01.spring.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BancoRequest {
	@NotNull(message = "El nombre del titular no puede ser nulo.")
	@Size(min = 3, max = 50, message = "El nombre del titular debe tener entre 3 y 50 caracteres.")
	private String nombreTitular;

	@NotNull(message = "El número de tarjeta no puede ser nulo.")
	@Pattern(regexp = "^[0-9]{16}$", message = "El número de tarjeta debe tener exactamente 16 dígitos.")
	private String numeroTarjeta;

	@NotNull(message = "El mes de caducidad no puede ser nulo.")
	@Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "El mes de caducidad debe ser un valor entre 01 y 12.")
	private String mesCaducidad;

	@NotNull(message = "El año de caducidad no puede ser nulo.")
	@Pattern(regexp = "^(20[2-9][0-9])$", message = "El año de caducidad debe ser un año válido (mayor o igual a 2024).")
	private String yearCaducidad;

	@NotNull(message = "El CVV no puede ser nulo.")
	@Pattern(regexp = "^[0-9]{3}$", message = "El CVV debe tener exactamente 3 dígitos.")
	private String cvv;

	private String emisor;

	private String concepto;

	@Positive(message = "La cantidad debe ser mayor a 0.")
	private BigDecimal cantidad;

	// Getters and Setters
	public String getNombreTitular() {
		return nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getMesCaducidad() {
		return mesCaducidad;
	}

	public void setMesCaducidad(String mesCaducidad) {
		this.mesCaducidad = mesCaducidad;
	}

	public String getYearCaducidad() {
		return yearCaducidad;
	}

	public void setYearCaducidad(String yearCaducidad) {
		this.yearCaducidad = yearCaducidad;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "BancoRequest [nombreTitular=" + nombreTitular + ", numeroTarjeta=" + numeroTarjeta + ", mesCaducidad="
				+ mesCaducidad + ", yearCaducidad=" + yearCaducidad + ", cvv=" + cvv + ", emisor=" + emisor
				+ ", concepto=" + concepto + ", cantidad=" + cantidad + "]";
	}
}
