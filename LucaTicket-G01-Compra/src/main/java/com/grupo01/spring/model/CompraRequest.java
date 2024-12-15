package com.grupo01.spring.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.grupo01.spring.model.BancoRequest;

public class CompraRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	@NotNull(message = "El ID del evento no puede estar vacío")
	private UUID eventId;
	
	@NotBlank(message = "El correo electronico no puede estar vacio")
	private String userMail;
	
    @NotNull(message = "La información de la tarjeta no puede estar vacía")
	private BancoRequest tarjeta;

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

	public BancoRequest getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(BancoRequest tarjeta) {
		this.tarjeta = tarjeta;
	}

	@Override
	public String toString() {
		return "CompraRequest [eventId=" + eventId + ", userMail=" + userMail + "]";
	}	    
			
}
