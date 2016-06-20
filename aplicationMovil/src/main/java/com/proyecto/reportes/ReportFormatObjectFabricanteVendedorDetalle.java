package com.proyecto.reportes;

import java.util.List;

public class ReportFormatObjectFabricanteVendedorDetalle {

	private String fabricante;
	private List<ReportFormatObjectFabricanteVendedorDetalleArticulo> articulos;
	private double totalCantFB, totalCantNC, totalImporteFB, totalImporteNC, totalCantidad, totalImporte;
	
	
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	public List<ReportFormatObjectFabricanteVendedorDetalleArticulo> getArticulos() {
		return articulos;
	}
	public void setArticulos(
			List<ReportFormatObjectFabricanteVendedorDetalleArticulo> articulos) {
		this.articulos = articulos;
	}
	public double getTotalCantFB() {
		return totalCantFB;
	}
	public void setTotalCantFB(double totalCantFB) {
		this.totalCantFB = totalCantFB;
	}
	public double getTotalCantNC() {
		return totalCantNC;
	}
	public void setTotalCantNC(double totalCantNC) {
		this.totalCantNC = totalCantNC;
	}
	public double getTotalImporteFB() {
		return totalImporteFB;
	}
	public void setTotalImporteFB(double totalImporteFB) {
		this.totalImporteFB = totalImporteFB;
	}
	public double getTotalImporteNC() {
		return totalImporteNC;
	}
	public void setTotalImporteNC(double totalImporteNC) {
		this.totalImporteNC = totalImporteNC;
	}
	public double getTotalCantidad() {
		return totalCantidad;
	}
	public void setTotalCantidad(double totalCantidad) {
		this.totalCantidad = totalCantidad;
	}
	public double getTotalImporte() {
		return totalImporte;
	}
	public void setTotalImporte(double totalImporte) {
		this.totalImporte = totalImporte;
	}
	
}
