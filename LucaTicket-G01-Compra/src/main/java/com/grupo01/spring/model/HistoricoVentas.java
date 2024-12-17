package com.grupo01.spring.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historicoventas")
public class HistoricoVentas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_event", columnDefinition = "UUID", nullable = false)
    private UUID idEvent;

    @Column(name = "precio_medio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioMedio;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;


    public UUID getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(UUID idEvent) {
        this.idEvent = idEvent;
    }

    public BigDecimal getPrecioMedio() {
        return precioMedio;
    }

    public void setPrecioMedio(BigDecimal precioMedio) {
        this.precioMedio = precioMedio;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HistoricoVentas{" +
                "idEvent=" + idEvent +
                ", precioMedio=" + precioMedio +
                ", timestamp=" + timestamp +
                '}';
    }
}
