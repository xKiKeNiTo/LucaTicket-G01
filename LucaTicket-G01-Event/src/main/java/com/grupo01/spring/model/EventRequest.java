package com.grupo01.spring.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

public class EventRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "El nombre del evento no puede estar vacío")
	private String nombre;

	private String descripcion;

	@NotNull(message = "La fecha del evento no puede estar vacía")
	private LocalDate fechaEvento;

	@NotNull(message = "La hora del evento no puede estar vacía")
	private LocalTime horaEvento;

	@NotNull(message = "El precio mínimo del evento no puede estar vacío")
	private BigDecimal precioMaximo;

	@NotNull(message = "El precio máximo del evento no puede estar vacío")
	private BigDecimal precioMinimo;

	@NotNull(message = "La localidad del evento no puede estar vacía")
	private Localidad localidad;

	private String nombreRecinto;

	private String generoMusica;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(LocalDate fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public LocalTime getHoraEvento() {
		return horaEvento;
	}

	public void setHoraEvento(LocalTime horaEvento) {
		this.horaEvento = horaEvento;
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

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getNombreRecinto() {
		return nombreRecinto;
	}

	public void setNombreRecinto(String nombreRecinto) {
		this.nombreRecinto = nombreRecinto;
	}

	public String getGeneroMusica() {
		return generoMusica;
	}

	public void setGeneroMusica(String generoMusica) {
		this.generoMusica = generoMusica;
	}

}
