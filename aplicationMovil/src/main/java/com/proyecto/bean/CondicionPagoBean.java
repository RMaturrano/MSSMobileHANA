package com.proyecto.bean;


public class CondicionPagoBean {

	private String numeroCondicion, descripcionCondicion, diasExtra;
	
	@Override
	public String toString() {
		return this.descripcionCondicion;
	}
	
	public String getNumeroCondicion() {
		return numeroCondicion;
	}

	public void setNumeroCondicion(String numeroCondicion) {
		this.numeroCondicion = numeroCondicion;
	}

	public String getDescripcionCondicion() {
		return descripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		this.descripcionCondicion = descripcionCondicion;
	}

	public String getDiasExtra() {
		return diasExtra;
	}

	public void setDiasExtra(String diasExtra) {
		this.diasExtra = diasExtra;
	}
	
	
	
}
