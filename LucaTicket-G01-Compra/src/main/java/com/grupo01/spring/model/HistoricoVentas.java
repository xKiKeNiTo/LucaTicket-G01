package com.grupo01.spring.model;

import jakarta.persistence.*;
<<<<<<< HEAD
=======

>>>>>>> feature-moha
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
<<<<<<< HEAD
@Table(name = "historico_ventas")
public class HistoricoVentas {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "id_evento", nullable = false)
	private UUID idEvento;

	@Column(name = "precio_medio", nullable = false)
	private BigDecimal precioMedio;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	// Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(UUID idEvento) {
		this.idEvento = idEvento;
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
		return "HistoricoVentas [id=" + id + ", idEvento=" + idEvento + ", precioMedio=" + precioMedio + ", timestamp="
				+ timestamp + "]";
	}

=======
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
>>>>>>> feature-moha
}
