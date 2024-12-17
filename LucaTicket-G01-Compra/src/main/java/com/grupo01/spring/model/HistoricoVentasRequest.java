package com.grupo01.spring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class HistoricoVentasRequest {

	@NotNull(message = "El ID del evento no puede ser nulo.")
	private UUID idEvento;

	@NotNull(message = "El precio promedio no puede ser nulo.")
	@Positive(message = "El precio promedio debe ser mayor que 0.")
	private BigDecimal precioPromedio;

	@NotNull(message = "El timestamp no puede ser nulo.")
	private LocalDateTime timestamp;

	// Constructor vacío
	public HistoricoVentasRequest() {
	}

	// Constructor con todos los campos
	public HistoricoVentasRequest(UUID idEvento, BigDecimal precioPromedio, LocalDateTime timestamp) {
		this.idEvento = idEvento;
		this.precioPromedio = precioPromedio;
		this.timestamp = timestamp;
	}

	// Getters y Setters
	public UUID getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(UUID idEvento) {
		this.idEvento = idEvento;
	}

	public BigDecimal getPrecioPromedio() {
		return precioPromedio;
	}

	public void setPrecioPromedio(BigDecimal precioPromedio) {
		this.precioPromedio = precioPromedio;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "HistoricoVentasRequest [idEvento=" + idEvento + ", precioPromedio=" + precioPromedio + ", timestamp="
				+ timestamp + "]";
	}

}
