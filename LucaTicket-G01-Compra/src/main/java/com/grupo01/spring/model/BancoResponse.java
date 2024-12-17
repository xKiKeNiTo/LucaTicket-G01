package com.grupo01.spring.model;

public class BancoResponse {
	private String codigo;
	private String mensaje;
	private boolean success;
	private String emisor;
    private String concepto;

	// Getters and Setters
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

	 @Override
	    public String toString() {
	        return "BancoResponse{" +
	                "codigo='" + codigo + '\'' +
	                ", emisor='" + emisor + '\'' +
	                ", concepto='" + concepto + '\'' +
	                '}';
	    }
	
}
