package com.grupo01.spring.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class EventRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String descripcion;
	private LocalDate fechaEvento;
	private LocalTime horaEvento;
	private BigDecimal precioMaximo;
	private BigDecimal precioMinimo;
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
