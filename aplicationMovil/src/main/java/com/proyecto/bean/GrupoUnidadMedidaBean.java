package com.proyecto.bean;

import java.util.ArrayList;

public class GrupoUnidadMedidaBean {

	private String codigo, nombre;
	private ArrayList<UnidadMedidaBean> unidadMedida;

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

	public ArrayList<UnidadMedidaBean> getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(ArrayList<UnidadMedidaBean> unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

}
