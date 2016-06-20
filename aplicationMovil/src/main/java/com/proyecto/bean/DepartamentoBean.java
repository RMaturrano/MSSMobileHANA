package com.proyecto.bean;

import java.util.ArrayList;

public class DepartamentoBean {

	private String codigo, nombre, pais;
	private ArrayList<ProvinciaBean> provincias;
	
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

	public ArrayList<ProvinciaBean> getProvincias() {
		return provincias;
	}

	public void setProvincias(ArrayList<ProvinciaBean> provincias) {
		this.provincias = provincias;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

}
