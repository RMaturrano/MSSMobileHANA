package com.proyecto.bean;

public class DireccionBean {

	private String codigoCliente;
	private String TipoDireccion;
	private String IDDireccion;
	private String Pais;
	private String Departamento;
	private String Provincia;
	private String Distrito;
	private String Calle;
	private String referencia;
	private String nombreCalle;
	private String latitud;
	private String longitud;
	private int utilId;
	private boolean principal;

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getTipoDireccion() {
		return TipoDireccion;
	}

	public void setTipoDireccion(String tipoDireccion) {
		TipoDireccion = tipoDireccion;
	}

	public String getIDDireccion() {
		return IDDireccion;
	}

	public void setIDDireccion(String iDDireccion) {
		IDDireccion = iDDireccion;
	}

	public String getPais() {
		return Pais;
	}

	public void setPais(String pais) {
		Pais = pais;
	}

	public String getDepartamento() {
		return Departamento;
	}

	public void setDepartamento(String departamento) {
		Departamento = departamento;
	}

	public String getProvincia() {
		return Provincia;
	}

	public void setProvincia(String provincia) {
		Provincia = provincia;
	}

	public String getDistrito() {
		return Distrito;
	}

	public void setDistrito(String distrito) {
		Distrito = distrito;
	}

	public String getCalle() {
		return Calle;
	}

	public void setCalle(String calle) {
		Calle = calle;
	}

	public int getUtilId() {
		return utilId;
	}

	public void setUtilId(int utilId) {
		this.utilId = utilId;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombreCalle() {
		return nombreCalle;
	}

	public void setNombreCalle(String nombreCalle) {
		this.nombreCalle = nombreCalle;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
}
