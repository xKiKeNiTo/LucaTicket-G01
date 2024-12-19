package com.grupo01.spring.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventResponse {
    private UUID idEvento;

    @JsonProperty("nombre")
    private String nombreEvento;

    private BigDecimal precioMinimo;
    private BigDecimal precioMaximo;

    // Nuevos campos
    private String localidad;
    private String nombreRecinto;

    // Getters
    public UUID getIdEvento() {
        return idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public BigDecimal getPrecioMinimo() {
        return precioMinimo;
    }

    public BigDecimal getPrecioMaximo() {
        return precioMaximo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getNombreRecinto() {
        return nombreRecinto;
    }

    // Setters
    public void setIdEvento(UUID idEvento) {
        this.idEvento = idEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public void setPrecioMinimo(BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public void setPrecioMaximo(BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setNombreRecinto(String nombreRecinto) {
        this.nombreRecinto = nombreRecinto;
    }
    public EventResponse(UUID idEvento, BigDecimal precioMinimo, BigDecimal precioMaximo) {
        this.idEvento = idEvento;
        this.precioMinimo = precioMinimo;
        this.precioMaximo = precioMaximo;
    }

    public EventResponse() {
    	
    }
    @Override
    public String toString() {
        return "EventResponse{" +
                "idEvento='" + idEvento + '\'' +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", precioMinimo=" + precioMinimo +
                ", precioMaximo=" + precioMaximo +
                ", localidad='" + localidad + '\'' +
                ", nombreRecinto='" + nombreRecinto + '\'' +
                '}';
    }
}
