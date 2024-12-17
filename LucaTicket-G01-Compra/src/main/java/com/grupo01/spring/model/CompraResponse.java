package com.grupo01.spring.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CompraResponse {
	private String timestampp;
	private String status;
	private String error;
	private String[] message;
	private Info info;
	private String infoadicional;

	public CompraResponse(String status, String error, String[] message, Info info, String infoadicional) {
		this.timestampp = LocalDateTime.now().toString();
		this.status = status;
		this.error = error;
		this.message = message;
		this.info = info;
		this.infoadicional = infoadicional;
	}

	// Clase estática para "info"
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Info {
		private String nombreTitular;
		private String numeroTarjeta;
		private String mesCaducidad;
		private String yearCaducidad;
		private String cvv;
		private String emisor;
		private String concepto;
		private BigDecimal cantidad;

		public Info(String nombreTitular, String numeroTarjeta, String mesCaducidad, String yearCaducidad, String cvv,
				String emisor, String concepto, BigDecimal cantidad) {
			this.nombreTitular = nombreTitular;
			this.numeroTarjeta = numeroTarjeta;
			this.mesCaducidad = mesCaducidad;
			this.yearCaducidad = yearCaducidad;
			this.cvv = cvv;
			this.emisor = emisor;
			this.concepto = concepto;
			this.cantidad = cantidad;
		}

		// Getters y setters
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
	}

	// Getters y setters para CompraResponse
	public String getTimestampp() {
		return timestampp;
	}

	public void setTimestampp(String timestampp) {
		this.timestampp = timestampp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getInfoadicional() {
		return infoadicional;
	}

	public void setInfoadicional(String infoadicional) {
		this.infoadicional = infoadicional;
	}
}
