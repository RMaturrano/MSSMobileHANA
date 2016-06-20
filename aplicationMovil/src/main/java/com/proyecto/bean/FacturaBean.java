package com.proyecto.bean;

import java.util.ArrayList;

public class FacturaBean {

	private String  tipo, clave, numero, referencia, socioNegocio, ListaPrecio, contacto, moneda, empleadoVenta,
					comentario, fechaContable, fechaDocumento, fechaVencimiento, direccionFiscal, direccionEntrega,
					condicionPago, indicador, subTotal, descuento, impuesto, total, saldo, utilPagoTotal;
	
	private int utilIcon;
	
	private ArrayList<FacturaDetalleBean> lineas;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getSocioNegocio() {
		return socioNegocio;
	}

	public void setSocioNegocio(String socioNegocio) {
		this.socioNegocio = socioNegocio;
	}

	public String getListaPrecio() {
		return ListaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		ListaPrecio = listaPrecio;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getEmpleadoVenta() {
		return empleadoVenta;
	}

	public void setEmpleadoVenta(String empleadoVenta) {
		this.empleadoVenta = empleadoVenta;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(String fechaContable) {
		this.fechaContable = fechaContable;
	}

	public String getFechaDocumento() {
		return fechaDocumento;
	}

	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getDireccionFiscal() {
		return direccionFiscal;
	}

	public void setDireccionFiscal(String direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public ArrayList<FacturaDetalleBean> getLineas() {
		return lineas;
	}

	public void setLineas(ArrayList<FacturaDetalleBean> lineas) {
		this.lineas = lineas;
	}

	public int getUtilIcon() {
		return utilIcon;
	}

	public void setUtilIcon(int utilIcon) {
		this.utilIcon = utilIcon;
	}

	public String getUtilPagoTotal() {
		return utilPagoTotal;
	}

	public void setUtilPagoTotal(String utilPagoTotal) {
		this.utilPagoTotal = utilPagoTotal;
	}
	
	
	
	
}
