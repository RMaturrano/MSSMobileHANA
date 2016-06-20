package com.proyecto.bean;

import java.util.ArrayList;

public class DistritoBean {

	private String codigo, nombre, provincia;
	private ArrayList<CalleBean> calles;
	
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

	public ArrayList<CalleBean> getCalles() {
		return calles;
	}

	public void setCalles(ArrayList<CalleBean> calles) {
		this.calles = calles;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

}
