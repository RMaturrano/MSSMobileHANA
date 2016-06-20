package com.proyecto.bean;

import java.util.ArrayList;

public class ProvinciaBean {

	private String codigo, nombre, departamento;
	private ArrayList<DistritoBean> distritos;
	
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

	public ArrayList<DistritoBean> getDistritos() {
		return distritos;
	}

	public void setDistritos(ArrayList<DistritoBean> distritos) {
		this.distritos = distritos;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

}
