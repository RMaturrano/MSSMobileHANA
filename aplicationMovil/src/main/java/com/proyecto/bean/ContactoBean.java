package com.proyecto.bean;

public class ContactoBean {
	
	private String idCon, nomCon, segNomCon, ApeCon,direccion, tel1Con, tel2Con, 
					telMovCon, emailCon, posicion, primerNombre;
	private boolean principal;
	private int utilId;

	@Override
	public String toString() {
		return this.nomCon;
	}
	
	public String getIdCon() {
		return idCon;
	}

	public void setIdCon(String idCon) {
		this.idCon = idCon;
	}

	public String getNomCon() {
		return nomCon;
	}

	public void setNomCon(String nomCon) {
		this.nomCon = nomCon;
	}

	public String getSegNomCon() {
		return segNomCon;
	}

	public void setSegNomCon(String segNomCon) {
		this.segNomCon = segNomCon;
	}

	public String getApeCon() {
		return ApeCon;
	}

	public void setApeCon(String apeCon) {
		ApeCon = apeCon;
	}

	public String getTel1Con() {
		return tel1Con;
	}

	public void setTel1Con(String tel1Con) {
		this.tel1Con = tel1Con;
	}

	public String getTel2Con() {
		return tel2Con;
	}

	public void setTel2Con(String tel2Con) {
		this.tel2Con = tel2Con;
	}

	public String getTelMovCon() {
		return telMovCon;
	}

	public void setTelMovCon(String telMovCon) {
		this.telMovCon = telMovCon;
	}

	public String getEmailCon() {
		return emailCon;
	}

	public void setEmailCon(String emailCon) {
		this.emailCon = emailCon;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public int getUtilId() {
		return utilId;
	}

	public void setUtilId(int utilId) {
		this.utilId = utilId;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	
	
	

}
