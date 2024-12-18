package com.grupo01.spring.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historicoventas")
public class HistoricoVentas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;

	@Column(name = "id_evento", nullable = false)
	private UUID idEvento;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	@Column(name = "precio_medio", nullable = false)
	private BigDecimal precioMedio;

	@Column(name = "precio_maximo", nullable = false)
	private BigDecimal precioMaximo;

	@Column(name = "precio_minimo", nullable = false)
	private BigDecimal precioMinimo;

	@Column(name = "conteo_ventas", nullable = false)
	private long conteoVentas;

	// Getters and Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(UUID idEvento) {
		this.idEvento = idEvento;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getPrecioMedio() {
		return precioMedio;
	}

	public void setPrecioMedio(BigDecimal precioMedio) {
		this.precioMedio = precioMedio;
	}

	public BigDecimal getPrecioMaximo() {
		return precioMaximo;
	}

	public void setPrecioMaximo(BigDecimal precioMaximo) {
		this.precioMaximo = precioMaximo;
	}

	public BigDecimal getPrecioMinimo() {
		return precioMinimo;
	}

	public void setPrecioMinimo(BigDecimal precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public long getConteoVentas() {
		return conteoVentas;
	}

	public void setConteoVentas(long conteoVentas) {
		this.conteoVentas = conteoVentas;
	}

	@Override
	public String toString() {
		return "HistoricoVentas [id=" + id + ", idEvento=" + idEvento + ", timestamp=" + timestamp + ", precioMedio="
				+ precioMedio + ", precioMaximo=" + precioMaximo + ", precioMinimo=" + precioMinimo + ", conteoVentas="
				+ conteoVentas + "]";
	}

}
