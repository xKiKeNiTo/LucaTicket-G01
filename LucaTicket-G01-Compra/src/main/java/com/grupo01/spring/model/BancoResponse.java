package com.grupo01.spring.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BancoResponse implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	@NotBlank(message = "El nombre del titular no puede estar vacío")
	private String nombreTitular;
	
    @NotBlank(message = "El número de tarjeta no puede estar vacío")
	private String numeroTarjeta; 
	
    @NotNull(message = "El mes no puede estar vacío")
	private int mesCaducidad; 
	
    @NotNull(message = "El año no puede estar vacío")
	private int yearCaducidad; 
	
    @NotNull(message = "El código CVV no puede estar vacío")
	private int cvv; 
	
    @NotBlank(message = "El emisor no puede estar vacío")
	private String emisor; 
    
    @NotBlank(message = "El concepto no puede estar vacío")
	private String concepto; 
	
    @NotNull(message = "La cantidad no puede estar vacía")
	private BigDecimal cantidad; 
    
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

	public int getMesCaducidad() {
		return mesCaducidad;
	}

	public void setMesCaducidad(int mesCaducidad) {
		this.mesCaducidad = mesCaducidad;
	}

	public int getYearCaducidad() {
		return yearCaducidad;
	}

	public void setYearCaducidad(int yearCaducidad) {
		this.yearCaducidad = yearCaducidad;
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
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
	
}

