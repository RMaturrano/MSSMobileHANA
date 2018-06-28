package com.proyecto.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PagoBean {

	private String tipo, clave, numero, socioNegocio, empleadoVenta,
			comentario, glosa, fechaContable, moneda, tipoPago,
			transferenciaCuenta, transferenciaReferencia, transferenciaImporte,
			efectivoCuenta, efectivoImporte, creadoMovil, claveMovil,
			ChequeCuenta,ChequeBanco,ChequeVencimiento,ChequeImporte,ChequeNumero,
			estadoRegistroMovil, nombreSocioNegocio, TransaccionMovil;
	
	private int utilIcon,utilIcon2;

	public String getChequeCuenta() {
		return ChequeCuenta;
	}

	public void setChequeCuenta(String chequeCuenta) {
		ChequeCuenta = chequeCuenta;
	}

	public String getChequeBanco() {
		return ChequeBanco;
	}

	public void setChequeBanco(String chequeBanco) {
		ChequeBanco = chequeBanco;
	}

	public String getChequeVencimiento() {
		return ChequeVencimiento;
	}

	public void setChequeVencimiento(String chequeVencimiento) {
		ChequeVencimiento = chequeVencimiento;
	}

	public String getChequeImporte() {
		return ChequeImporte;
	}

	public void setChequeImporte(String chequeImporte) {
		ChequeImporte = chequeImporte;
	}

	public String getChequeNumero() {
		return ChequeNumero;
	}

	public void setChequeNumero(String chequeNumero) {
		ChequeNumero = chequeNumero;
	}

	public String getCreadoMovil() {
		return creadoMovil;
	}

	public void setCreadoMovil(String creadoMovil) {
		this.creadoMovil = creadoMovil;
	}

	public String getClaveMovil() {
		return claveMovil;
	}

	public void setClaveMovil(String claveMovil) {
		this.claveMovil = claveMovil;
	}

	public String getEstadoRegistroMovil() {
		return estadoRegistroMovil;
	}

	public void setEstadoRegistroMovil(String estadoRegistroMovil) {
		this.estadoRegistroMovil = estadoRegistroMovil;
	}

	private ArrayList<PagoDetalleBean> lineas;

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

	public String getSocioNegocio() {
		return socioNegocio;
	}

	public void setSocioNegocio(String socioNegocio) {
		this.socioNegocio = socioNegocio;
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

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	public String getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(String fechaContable) {
		this.fechaContable = fechaContable;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getTransferenciaCuenta() {
		return transferenciaCuenta;
	}

	public void setTransferenciaCuenta(String transferenciaCuenta) {
		this.transferenciaCuenta = transferenciaCuenta;
	}

	public String getTransferenciaReferencia() {
		return transferenciaReferencia;
	}

	public void setTransferenciaReferencia(String transferenciaReferencia) {
		this.transferenciaReferencia = transferenciaReferencia;
	}

	public String getTransferenciaImporte() {
		return transferenciaImporte;
	}

	public void setTransferenciaImporte(String transferenciaImporte) {
		this.transferenciaImporte = transferenciaImporte;
	}

	public String getEfectivoCuenta() {
		return efectivoCuenta;
	}

	public void setEfectivoCuenta(String efectivoCuenta) {
		this.efectivoCuenta = efectivoCuenta;
	}

	public String getEfectivoImporte() {
		return efectivoImporte;
	}

	public void setEfectivoImporte(String efectivoImporte) {
		this.efectivoImporte = efectivoImporte;
	}

	public ArrayList<PagoDetalleBean> getLineas() {
		return lineas;
	}

	public void setLineas(ArrayList<PagoDetalleBean> lineas) {
		this.lineas = lineas;
	}

	public int getUtilIcon() {
		return utilIcon;
	}

	public void setUtilIcon(int utilIcon) {
		this.utilIcon = utilIcon;
	}

	public String getNombreSocioNegocio() {
		return nombreSocioNegocio;
	}

	public void setNombreSocioNegocio(String nombreSocioNegocio) {
		this.nombreSocioNegocio = nombreSocioNegocio;
	}

	public int getUtilIcon2() {
		return utilIcon2;
	}

	public void setUtilIcon2(int utilIcon2) {
		this.utilIcon2 = utilIcon2;
	}

	public String getTransaccionMovil() {
		return TransaccionMovil;
	}

	public void setTransaccionMovil(String transaccionMovil) {
		TransaccionMovil = transaccionMovil;
	}

	public static JSONObject transformPRToJSON(PagoBean ov, String sociedad){
		try	{

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ClaveMovil", ov.getClaveMovil());
			jsonObject.put("TransaccionMovil", ov.getTransaccionMovil());
			jsonObject.put("SocioNegocio", ov.getSocioNegocio());
			jsonObject.put("EmpleadoVenta", ov.getEmpleadoVenta());
			jsonObject.put("Comentario", ov.getComentario() != null ? ov.getComentario() : "");
			jsonObject.put("Glosa", ov.getGlosa() != null ? ov.getGlosa(): "");
			jsonObject.put("FechaContable", ov.getFechaContable());
			jsonObject.put("TipoPago", ov.getTipoPago() != null ? ov.getTipoPago() : "");
			jsonObject.put("Moneda", ov.getMoneda());
			jsonObject.put("ChequeCuenta", ov.getChequeCuenta() != null ? ov.getChequeCuenta() : "");
			jsonObject.put("ChequeBanco", ov.getChequeBanco() != null ? ov.getChequeBanco() : "");
			jsonObject.put("ChequeVencimiento", ov.getChequeVencimiento() != null ? ov.getChequeVencimiento() : "");
			jsonObject.put("ChequeImporte", ov.getChequeImporte() != null ? Double.parseDouble(ov.getChequeImporte()) : 0);
			jsonObject.put("ChequeNumero", ov.getChequeNumero() != null ? Integer.parseInt(ov.getChequeNumero()) : 0);
			jsonObject.put("TransferenciaCuenta", ov.getTransferenciaCuenta() != null ? ov.getTransferenciaCuenta() : "");
			jsonObject.put("TransferenciaReferencia", ov.getTransferenciaReferencia() != null ? ov.getTransferenciaReferencia() : "");
			jsonObject.put("TransferenciaImporte", ov.getTransferenciaImporte() != null ?
					Double.parseDouble(ov.getTransferenciaImporte()) : 0);
			jsonObject.put("EfectivoCuenta", ov.getEfectivoCuenta() != null ? ov.getEfectivoCuenta() : "");
			jsonObject.put("EfectivoImporte", ov.getEfectivoImporte() != null ?
					Double.parseDouble(ov.getEfectivoImporte()) : 0);
			jsonObject.put("Empresa", Integer.parseInt(sociedad));

			JSONArray lines = new JSONArray();

			for (PagoDetalleBean line: ov.getLineas()) {

				if(line.getFacturaCliente() != null){
					JSONObject jsonLine = new JSONObject();
					jsonLine.put("FacturaCliente", Integer.parseInt(line.getFacturaCliente()));
					jsonLine.put("Importe", Double.parseDouble(line.getImporte()));
					lines.put(jsonLine);
				}
			}

			jsonObject.put("Lines", lines);

			return jsonObject;

		}catch (Exception e){
			return  null;
		}
	}

}
