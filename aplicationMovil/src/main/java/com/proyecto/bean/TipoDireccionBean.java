package com.proyecto.bean;

import java.util.ArrayList;

public class TipoDireccionBean {

	private String codigo, descripcion;
	private TipoDireccionBean b = null;
	
	@Override
	public String toString() {
		return this.codigo + " - "+descripcion;
	}
	
	public ArrayList<TipoDireccionBean> lista(){
		
		ArrayList<TipoDireccionBean> list = new ArrayList<TipoDireccionBean>();
		b = new TipoDireccionBean();
		b.setCodigo("B");
		b.setDescripcion("Fiscal");
		list.add(b);
		
		b = new TipoDireccionBean();
		b.setCodigo("S");
		b.setDescripcion("Entrega");
		list.add(b);
		
		return list;
		
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
