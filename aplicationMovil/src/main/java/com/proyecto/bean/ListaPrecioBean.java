package com.proyecto.bean;

import java.util.ArrayList;

public class ListaPrecioBean {

	private String codigo, nombre;
	private ArrayList<PrecioBean> precio;
	
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

	public ArrayList<PrecioBean> getPrecio() {
		return precio;
	}

	public void setPrecio(ArrayList<PrecioBean> precio) {
		this.precio = precio;
	}

}
