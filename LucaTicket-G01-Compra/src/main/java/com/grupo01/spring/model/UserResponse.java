package com.grupo01.spring.model;

public class UserResponse {
    private String idUsuario;
    private String nombre;
    private String email;

    // Getters
    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public UserResponse(String idUsuario, String nombre, String email) {
		super();
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.email = email;
	}    
    
}
