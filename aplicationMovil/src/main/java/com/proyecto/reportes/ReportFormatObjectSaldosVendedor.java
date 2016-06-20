package com.proyecto.reportes;

import java.util.List;

public class ReportFormatObjectSaldosVendedor {
	
	private String  empleado, empresa, direccion;
	private List<ReportFormatObjectSaldosVendedorDetail> detalles;
	private double total, pagado, saldo, totalRecibo;

	public double getPagado() {
		return pagado;
	}

	public void setPagado(double pagado) {
		this.pagado = pagado;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public List<ReportFormatObjectSaldosVendedorDetail> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<ReportFormatObjectSaldosVendedorDetail> detalles) {
		this.detalles = detalles;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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

	public double getTotalRecibo() {
		return totalRecibo;
	}

	public void setTotalRecibo(double totalRecibo) {
		this.totalRecibo = totalRecibo;
	}

}
