package com.grupo01.spring.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class EventResponse implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2L;
	
    private Long id;
	
	private String nombre;
	private String descripcion;
	private Date fechaEvento;
	private Date horaEvento;
	private BigDecimal precioMaximo;
	private BigDecimal precioMinimo;
	private Localidad localidad;
	private String nombreRecinto;
	private String generoMusica;
	
	  public Long getId() {
	    return id;
	}
	
    public void setId(Long id) {
        this.id = id;
    }
	
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
	public Date getFechaEvento() {
		return fechaEvento;
	}
	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
	public Date getHoraEvento() {
		return horaEvento;
	}
	public void setHoraEvento(Date horaEvento) {
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
