package com.proyecto.bean;


public class MonedaBean {

	private String codigo, descripcion;


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
