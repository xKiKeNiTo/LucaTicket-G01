package com.grupo01.spring.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_compra", columnDefinition = "UUID", nullable = false, unique = true, updatable = false)
    private UUID idCompra;

    @Column(name = "id_event", columnDefinition = "UUID", nullable = false)
    private UUID idEvent;

    @Column(name = "user_mail", nullable = false, length = 255)
    private String userMail;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Override
	public String toString() {
		return "Compra [idCompra=" + idCompra + ", idEvent=" + idEvent + ", userMail=" + userMail + ", precio=" + precio
				+ ", fechaCompra=" + fechaCompra + "]";
	}

	// Getters y Setters
    public UUID getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(UUID idCompra) {
        this.idCompra = idCompra;
    }

    public UUID getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(UUID idEvent) {
        this.idEvent = idEvent;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
