package com.grupo01.spring.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventResponse {
	private String idEvento;
	@JsonProperty("nombre")
	private String nombreEvento;
	private BigDecimal precioMinimo;
	private BigDecimal precioMaximo;

	// Getters
	public String getIdEvento() {
		return idEvento;
	}

	public String getNombreEvento() {
		return nombreEvento;
	}

	public BigDecimal getPrecioMinimo() {
		return precioMinimo;
	}

	public BigDecimal getPrecioMaximo() {
		return precioMaximo;
	}

	// Setters
	public void setIdEvento(String idEvento) {
		this.idEvento = idEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}

	public void setPrecioMinimo(BigDecimal precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public void setPrecioMaximo(BigDecimal precioMaximo) {
		this.precioMaximo = precioMaximo;
	}

	@Override
	public String toString() {
		return "EventResponse{" + "idEvento='" + idEvento + '\'' + ", nombreEvento='" + nombreEvento + '\''
				+ ", precioMinimo=" + precioMinimo + ", precioMaximo=" + precioMaximo + '}';
	}
}
