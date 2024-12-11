package com.grupo01.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

	@NotBlank(message = "El nombre del evento no puede estar vacío")
	private String nombre;

	private String descripcion;

	@NotNull(message = "La fecha del evento no puede estar vacía")
	private LocalDate fechaEvento;

	@NotNull(message = "La hora del evento no puede estar vacía")
	private LocalTime horaEvento;

	@Positive(message = "El precio mínimo debe ser mayor que 0")
	@NotNull(message = "El precio mínimo del evento no puede estar vacío")
	private BigDecimal precioMinimo;

	@Positive(message = "El precio máximo debe ser mayor que 0")
	@NotNull(message = "El precio máximo del evento no puede estar vacío")
	private BigDecimal precioMaximo;

	@AssertTrue(message = "El precio mínimo debe ser menor que el precio máximo")
	public boolean isPrecioMinimoMenorQuePrecioMaximo() {
		return precioMinimo != null && precioMaximo != null && precioMinimo.compareTo(precioMaximo) < 0;
	}

	@NotNull(message = "La localidad del evento no puede estar vacía")
	private Localidad localidad;

	private String nombreRecinto;

	private String generoMusical;

	public Event() {
	}

	public Event(UUID id, String nombre, String descripcion, LocalDate fechaEvento, LocalTime horaEvento,
			BigDecimal precioMinimo, BigDecimal precioMaximo, Localidad localidad, String nombreRecinto,
			String generoMusical) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaEvento = fechaEvento;
		this.horaEvento = horaEvento;
		this.precioMinimo = precioMinimo;
		this.precioMaximo = precioMaximo;
		this.localidad = localidad;
		this.nombreRecinto = nombreRecinto;
		this.generoMusical = generoMusical;
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

	public LocalDate getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(LocalDate fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public LocalTime getHoraEvento() {
		return horaEvento;
	}

	public void setHoraEvento(LocalTime horaEvento) {
		this.horaEvento = horaEvento;
	}

	public BigDecimal getPrecioMinimo() {
		return precioMinimo;
	}

	public void setPrecioMinimo(BigDecimal precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public BigDecimal getPrecioMaximo() {
		return precioMaximo;
	}

	public void setPrecioMaximo(BigDecimal precioMaximo) {
		this.precioMaximo = precioMaximo;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getNombreRecinto() {
		return nombreRecinto;
	}

	public void setNombreRecinto(String nombreRecinto) {
		this.nombreRecinto = nombreRecinto;
	}

	public String getGeneroMusical() {
		return generoMusical;
	}

	public void setGeneroMusical(String generoMusical) {
		this.generoMusical = generoMusical;
	}

	@Override
	public String toString() {
		return "Event{" + "id=" + id + ", nombre='" + nombre + '\'' + ", descripcion='" + descripcion + '\''
				+ ", fechaEvento=" + fechaEvento + ", horaEvento=" + horaEvento + ", precioMinimo=" + precioMinimo
				+ ", precioMaximo=" + precioMaximo + ", localidad='" + localidad + '\'' + ", nombreRecinto='"
				+ nombreRecinto + '\'' + ", generoMusical='" + generoMusical + '\'' + '}';
	}
}
