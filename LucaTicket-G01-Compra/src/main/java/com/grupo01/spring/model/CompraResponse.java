package com.grupo01.spring.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompraResponse implements Serializable {
	
	  /**
	   * 
	   */
		private static final long serialVersionUID = 4L;
		
		@NotNull(message = "El ID del evento no puede estar vacío")
		private UUID eventId;
		
		@NotBlank(message = "El correo electronico no puede estar vacio")
		private String userMail;
		
		@NotBlank(message = "El nombre del evento no puede estar vacío")
		private String nombreEvento;
		
		@NotNull(message = "La cantidad no puede estar vacía")
		private int cantidad;

		@NotNull(message = "La fecha no puede estar vacía")
		private LocalDate fechaCompra;

		public UUID getEventId() {
			return eventId;
		}

		public void setEventId(UUID eventId) {
			this.eventId = eventId;
		}

		public String getUserMail() {
			return userMail;
		}

		public void setUserMail(String userMail) {
			this.userMail = userMail;
		}

		public String getNombreEvento() {
			return nombreEvento;
		}

		public void setNombreEvento(String nombreEvento) {
			this.nombreEvento = nombreEvento;
		}

		public int getCantidad() {
			return cantidad;
		}

		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}

		public LocalDate getFechaCompra() {
			return fechaCompra;
		}

		public void setFechaCompra(LocalDate fechaCompra) {
			this.fechaCompra = fechaCompra;
		}								

}
