package com.proyecto.bean;

import java.util.ArrayList;

public class PaisBean {

	private String codigo, nombre;
	private ArrayList<DepartamentoBean> departamentos;
	
	@Override
	public String toString() {
		return this.nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public ArrayList<DepartamentoBean> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(ArrayList<DepartamentoBean> departamentos) {
		this.departamentos = departamentos;
	}

}
