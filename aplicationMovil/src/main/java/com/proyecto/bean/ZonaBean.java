package com.proyecto.bean;

public class ZonaBean {

	private String codigo, nombre;
	
	@Override
	public String toString() {
		return this.nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
