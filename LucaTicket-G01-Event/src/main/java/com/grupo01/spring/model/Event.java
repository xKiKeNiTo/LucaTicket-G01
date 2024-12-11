package com.grupo01.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Clase Event Modelo con constructor, getters, setters y toString
 *
 * @version 1
 * @author Moha
 */

@Entity
@Table(name = "events")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull(message = "El nombre del evento no puede estar vacío")
	private String nombre;

	private String descripcion;

	@NotNull(message = "La fecha del evento no puede estar vacía")
	private LocalDate fecha_evento;

	@NotNull(message = "La hora del evento no puede estar vacía")
	private LocalTime hora_evento;

	@NotNull(message = "El precio mínimo del evento no puede estar vacío")
	private BigDecimal precio_minimo;

	@NotNull(message = "El precio máximo del evento no puede estar vacío")
	private BigDecimal precio_maximo;

	@NotNull(message = "La localidad del evento no puede estar vacía")
	private Localidad localidad;

	private String nombre_recinto;

	private String genero_musical;

	public Event() {
	}

	public Event(UUID id, String nombre, String descripcion, LocalDate fecha_evento, LocalTime hora_evento,
			BigDecimal precio_minimo, BigDecimal precio_maximo, Localidad localidad, String nombre_recinto,
			String genero_musical) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha_evento = fecha_evento;
		this.hora_evento = hora_evento;
		this.precio_minimo = precio_minimo;
		this.precio_maximo = precio_maximo;
		this.localidad = localidad;
		this.nombre_recinto = nombre_recinto;
		this.genero_musical = genero_musical;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	public LocalDate getFecha_evento() {
		return fecha_evento;
	}

	public void setFecha_evento(LocalDate fecha_evento) {
		this.fecha_evento = fecha_evento;
	}

	public LocalTime getHora_evento() {
		return hora_evento;
	}

	public void setHora_evento(LocalTime hora_evento) {
		this.hora_evento = hora_evento;
	}

	public BigDecimal getPrecio_minimo() {
		return precio_minimo;
	}

	public void setPrecio_minimo(BigDecimal precio_minimo) {
		this.precio_minimo = precio_minimo;
	}

	public BigDecimal getPrecio_maximo() {
		return precio_maximo;
	}

	public void setPrecio_maximo(BigDecimal precio_maximo) {
		this.precio_maximo = precio_maximo;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getNombre_recinto() {
		return nombre_recinto;
	}

	public void setNombre_recinto(String nombre_recinto) {
		this.nombre_recinto = nombre_recinto;
	}

	public String getGenero_musical() {
		return genero_musical;
	}

	public void setGenero_musical(String genero_musical) {
		this.genero_musical = genero_musical;
	}

	@Override
	public String toString() {
		return "Event{" + "id=" + id + ", nombre='" + nombre + '\'' + ", descripcion='" + descripcion + '\''
				+ ", fecha_evento=" + fecha_evento + ", hora_evento=" + hora_evento + ", precio_minimo=" + precio_minimo
				+ ", precio_maximo=" + precio_maximo + ", localidad='" + localidad + '\'' + ", nombre_recinto='"
				+ nombre_recinto + '\'' + ", genero_musical='" + genero_musical + '\'' + '}';
	}
}
