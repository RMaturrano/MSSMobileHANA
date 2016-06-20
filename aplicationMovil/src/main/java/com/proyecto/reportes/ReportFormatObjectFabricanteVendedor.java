package com.proyecto.reportes;

import java.util.List;

public class ReportFormatObjectFabricanteVendedor {
	
	private String empleado, empresa, direccion;
	private List<ReportFormatObjectFabricanteVendedorDetalle> fabricantes;
	private double totalGenCanFB, totalGenCanNc, totalGenImporteFB, totalGenImporteNc, 
					totalGenCantidad, totalGenImporte;
	
	
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

	public double getTotalGenCanFB() {
		return totalGenCanFB;
	}
	public void setTotalGenCanFB(double totalGenCanFB) {
		this.totalGenCanFB = totalGenCanFB;
	}
	public double getTotalGenCanNc() {
		return totalGenCanNc;
	}
	public void setTotalGenCanNc(double totalGenCanNc) {
		this.totalGenCanNc = totalGenCanNc;
	}
	public double getTotalGenImporteFB() {
		return totalGenImporteFB;
	}
	public void setTotalGenImporteFB(double totalGenImporteFB) {
		this.totalGenImporteFB = totalGenImporteFB;
	}
	public double getTotalGenImporteNc() {
		return totalGenImporteNc;
	}
	public void setTotalGenImporteNc(double totalGenImporteNc) {
		this.totalGenImporteNc = totalGenImporteNc;
	}
	public double getTotalGenCantidad() {
		return totalGenCantidad;
	}
	public void setTotalGenCantidad(double totalGenCantidad) {
		this.totalGenCantidad = totalGenCantidad;
	}
	public double getTotalGenImporte() {
		return totalGenImporte;
	}
	public void setTotalGenImporte(double totalGenImporte) {
		this.totalGenImporte = totalGenImporte;
	}
	public List<ReportFormatObjectFabricanteVendedorDetalle> getFabricantes() {
		return fabricantes;
	}
	public void setFabricantes(List<ReportFormatObjectFabricanteVendedorDetalle> fabricantes) {
		this.fabricantes = fabricantes;
	}

}
