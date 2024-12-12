package com.grupo01.spring.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Email(message = "El correo electronico debe ser valido")
	@NotBlank(message = "El correo electronico no puede estar vacio")
	private String mail;

	@NotBlank(message = "El nombre del usuario no puede estar vacio")
	@Size(max = 50, message = "El nombre no puede tener mas de 50 caracteres")
	private String nombre;

	@NotBlank(message = "El apellido del usuario no puede estar vacio")
	@Size(max = 50, message = "El apellido no puede tener mas de 50 caracteres")
	private String apellido;

	@NotBlank(message = "La contraseña del usuario no puede estar vacia")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	private String contrasena;

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

	@Override
	public String toString() {
		return "UserRequestDTO [mail=" + mail + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}

	public UserRequest() {
		super();
	}

	public UserRequest(
			@Email(message = "El correo electronico debe ser valido") @NotBlank(message = "El correo electronico no puede estar vacio") String mail,
			@NotBlank(message = "El nombre del usuario no puede estar vacio") @Size(max = 50, message = "El nombre no puede tener mas de 50 caracteres") String nombre,
			@NotBlank(message = "El apellido del usuario no puede estar vacio") @Size(max = 50, message = "El apellido no puede tener mas de 50 caracteres") String apellido,
			@NotBlank(message = "La contraseña del usuario no puede estar vacia") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String contrasena) {
		super();
		this.mail = mail;
		this.nombre = nombre;
		this.apellido = apellido;
		this.contrasena = contrasena;
	}

}
