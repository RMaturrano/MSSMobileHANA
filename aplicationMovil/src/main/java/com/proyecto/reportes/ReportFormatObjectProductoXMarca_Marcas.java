package com.proyecto.reportes;

import java.util.List;

public class ReportFormatObjectProductoXMarca_Marcas {
	
	private String descripcion;
	private List<ReportFormatObjectProductoXMarca_Marcas_Detalles> detalles;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<ReportFormatObjectProductoXMarca_Marcas_Detalles> getDetalles() {
		return detalles;
	}
	public void setDetalles(
			List<ReportFormatObjectProductoXMarca_Marcas_Detalles> detalles) {
		this.detalles = detalles;
	}
	
}
