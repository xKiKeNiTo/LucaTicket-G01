package com.grupo01.spring.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Email(message = "El correo electronico debe ser valido")
	@NotBlank(message = "El correo electronico no puede estar vacio")
	private String mail;

	@NotBlank(message = "El nombre del usuario no puede estar vacio")
	private String nombre;

	@NotBlank(message = "El apellido del usuario no puede estar vacio")
	private String apellido;

	@NotNull(message = "La fecha de alta no puede estar vacia")
	private LocalDate fechaAlta;

	public UserResponse() {
	}

	public UserResponse(String mail, String nombre, String apellido, LocalDate fechaAlta) {
		this.mail = mail;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaAlta = fechaAlta;
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

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Override
	public String toString() {
		return "UserResponseDTO [mail=" + mail + ", nombre=" + nombre + ", apellido=" + apellido + ", fechaAlta="
				+ fechaAlta + "]";
	}

}
