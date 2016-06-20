package com.proyecto.bean;

import java.util.ArrayList;

public class TipoClienteBean {
	
	private String codigo, descripcion;
	private TipoClienteBean b = null;
	
	@Override
	public String toString() {
		return this.descripcion;
	}
	
	public ArrayList<TipoClienteBean> lista(){
		
		ArrayList<TipoClienteBean> list = new ArrayList<TipoClienteBean>();
		b = new TipoClienteBean();
		b.setCodigo("C");
		b.setDescripcion("Cliente");
		list.add(b);
		
		b = new TipoClienteBean();
		b.setCodigo("L");
		b.setDescripcion("Lead");
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
