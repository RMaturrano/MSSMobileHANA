package com.proyecto.bean;

public class DireccionBean {

	
	private String TipoDireccion,IDDireccion,Pais,Departamento,Provincia,Distrito,Calle, referencia, nombreCalle;
	private int utilId;
	private boolean principal;

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
	
	
	
}
