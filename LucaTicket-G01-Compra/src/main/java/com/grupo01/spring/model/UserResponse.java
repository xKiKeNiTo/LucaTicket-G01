package com.grupo01.spring.model;

public class UserResponse {
    private String mail;  // Debe coincidir con el campo "mail" del JSON
    private String nombre;  // Debe coincidir con el campo "nombre" del JSON
    private String apellido;  // Debe coincidir con el campo "apellido" del JSON
    private String fechaAlta;  // Debe coincidir con el campo "fechaAlta" del JSON

    // Getters y Setters
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

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
	public String toString() {
		return "UserResponse [mail=" + mail + ", nombre=" + nombre + ", apellido=" + apellido + ", fechaAlta="
				+ fechaAlta + "]";
	}
}
