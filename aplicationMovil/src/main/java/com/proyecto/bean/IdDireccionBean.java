package com.proyecto.bean;

import java.util.ArrayList;

public class IdDireccionBean {
	
	private String codigo, descripcion;
	private IdDireccionBean b = null;
	
	@Override
	public String toString() {
		return this.codigo + " - "+descripcion;
	}
	
	public ArrayList<IdDireccionBean> lista(){
		
		ArrayList<IdDireccionBean> list = new ArrayList<IdDireccionBean>();
		b = new IdDireccionBean();
		b.setCodigo("DIRECCION 1");
		b.setDescripcion("DIRECCION 1");
		list.add(b);
		
		b = new IdDireccionBean();
		b.setCodigo("DIRECCION 2");
		b.setDescripcion("DIRECCION 2");
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
