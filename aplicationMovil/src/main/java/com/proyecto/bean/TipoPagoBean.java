package com.proyecto.bean;

public class TipoPagoBean {

	private String tipoPago, TransferenciaCuenta, TransferenciaReferencia, TransferenciaImporte,
					EfectivoCuenta,EfectivoImporte,
					ChequeCuenta,ChequeBanco,ChequeVencimiento,ChequeImporte,ChequeNumero;

	
	
	
	public void clear() {
		tipoPago = "";
		TransferenciaCuenta = "";
		TransferenciaReferencia = "";
		TransferenciaImporte = "";
		EfectivoCuenta = "";
		EfectivoImporte = "";
		ChequeCuenta = "";
		ChequeBanco = "";
		ChequeVencimiento = "";
		ChequeImporte = "";
		ChequeNumero = "";
	}

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

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getTransferenciaCuenta() {
		return TransferenciaCuenta;
	}

	public void setTransferenciaCuenta(String transferenciaCuenta) {
		TransferenciaCuenta = transferenciaCuenta;
	}

	public String getTransferenciaReferencia() {
		return TransferenciaReferencia;
	}

	public void setTransferenciaReferencia(String transferenciaReferencia) {
		TransferenciaReferencia = transferenciaReferencia;
	}

	public String getTransferenciaImporte() {
		return TransferenciaImporte;
	}

	public void setTransferenciaImporte(String transferenciaImporte) {
		TransferenciaImporte = transferenciaImporte;
	}

	public String getEfectivoCuenta() {
		return EfectivoCuenta;
	}

	public void setEfectivoCuenta(String efectivoCuenta) {
		EfectivoCuenta = efectivoCuenta;
	}

	public String getEfectivoImporte() {
		return EfectivoImporte;
	}

	public void setEfectivoImporte(String efectivoImporte) {
		EfectivoImporte = efectivoImporte;
	}
	
}
