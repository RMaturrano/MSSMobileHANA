package com.proyecto.reportes;

import java.util.List;

public class ReportFormatObjectProductoXMarca {
	
	private String empleado, empresa, direccion;
	private List<ReportFormatObjectProductoXMarca_Marcas> marcas;
	
	public String getEmpleado() {
		return empleado;
	}
	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public List<ReportFormatObjectProductoXMarca_Marcas> getMarcas() {
		return marcas;
	}
	public void setMarcas(List<ReportFormatObjectProductoXMarca_Marcas> marcas) {
		this.marcas = marcas;
	}

}
