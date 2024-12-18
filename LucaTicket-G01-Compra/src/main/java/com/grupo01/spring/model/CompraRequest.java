package com.grupo01.spring.model;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CompraRequest {
    @Override
	public String toString() {
		return "CompraRequest [email=" + email + ", eventId=" + eventId + ", bancoRequest=" + bancoRequest + "]";
	}

	@NotNull
    private String email;

    @NotNull
    private UUID eventId;

    @NotNull
    @Valid
    private BancoRequest bancoRequest;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public BancoRequest getBancoRequest() {
        return bancoRequest;
    }

    public void setBancoRequest(BancoRequest bancoRequest) {
        this.bancoRequest = bancoRequest;
    }
}
