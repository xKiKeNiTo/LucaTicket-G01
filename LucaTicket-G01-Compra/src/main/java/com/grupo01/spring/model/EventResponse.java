package com.grupo01.spring.model;

import java.math.BigDecimal;

public class EventResponse {
    private String idEvento;
    private String nombreEvento;
    private BigDecimal precioEvento;

    // Getters
    public String getIdEvento() {
        return idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public BigDecimal getPrecioEvento() {
        return precioEvento;
    }

    // Setters
    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public void setPrecioEvento(BigDecimal precioEvento) {
        this.precioEvento = precioEvento;
    }
}
