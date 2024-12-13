package com.grupo01.spring.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="compras")
public class Compra {
	/**
	 * Clase Compra Modelo con constructor, getters, setters y toString
	 *
	 * @version 1
	 * @author raul_
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID idCompra;
	
	@NotNull(message = "El ID del evento no puede estar vacío")
	private UUID idEvent;
	
	@NotBlank(message = "El correo electronico no puede estar vacio")
	private String userMail;
	
	@Positive(message = "El precio debe ser mayor que 0")
	@NotNull(message = "El precio del evento no puede estar vacío")
	private BigDecimal precio;
	
	@NotNull(message = "La fecha de compra no puede estar vacía")
	private LocalDateTime fechaCompra;	
	
	public Compra() {
		super();
	}
				
	public Compra(UUID idCompra, UUID idEvent, String userMail, BigDecimal precio, LocalDateTime fechaCompra) {
		super();
		this.idCompra = idCompra;
		this.idEvent = idEvent;
		this.userMail = userMail;
		this.precio = precio;
		this.fechaCompra = fechaCompra;
	}

	public UUID getIdCompra() {
		return idCompra;
	}
	public void setIdCompra(UUID idCompra) {
		this.idCompra = idCompra;
	}
	public UUID getIdEvent() {
		return idEvent;
	}
	public void setIdEvent(UUID idEvent) {
		this.idEvent = idEvent;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public LocalDateTime getFechaCompra() {
		return fechaCompra;
	}
	public void setFechaCompra(LocalDateTime fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	@Override
	public String toString() {
		return "Compra [idCompra=" + idCompra + ", idEvent=" + idEvent + ", userMail=" + userMail + ", precio=" + precio
				+ ", fechaCompra=" + fechaCompra + "]";
	}
}
