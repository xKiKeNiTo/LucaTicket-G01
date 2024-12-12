package com.grupo01.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Clase User Modelo con constructor, getters, setters y toString
 */

@Entity
@Table(name = "users")
public class User {

	@Id
	private String mail;

	@NotBlank(message = "El nombre del usuario no puede estar vacio")
	private String nombre;

	@NotBlank(message = "El apellido del usuario no puede estar vacio")
	private String apellido;

	@NotBlank(message = "La contrasena del usuario no puede estar vacia")
	private String contrasena;
	
	@Column(nullable = false, updatable = false)
	private LocalDate fechaAlta;
	
	@PrePersist
	protected void onCreate() {
		this.fechaAlta = LocalDate.now();
	}
	

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public User() {
		super();
	}

	public User(String mail, String nombre, String apellido, String contrasena, LocalDate fechaAlta) {
		super();
		this.mail = mail;
		this.nombre = nombre;
		this.apellido = apellido;
		this.contrasena = contrasena;
		this.fechaAlta = fechaAlta;
	}

	@Override
	public String toString() {
		return "User [mail=" + mail + ", nombre=" + nombre + ", apellido=" + apellido + ", contrasena=" + contrasena
				+ ", fechaAlta=" + fechaAlta + "]";
	}

}
