package com.proyecto.bean;

import java.util.ArrayList;

public class SocioNegocioBean {
	
	
	private String tipoPersona, tipoDoc, codigo, nroDoc, nombRazSoc, apePat, apeMat,
			priNom, segNom, nomCom, grupo, moneda, tlf1, tlf2, tlfMov, correo, 
			condPago, comentario,
			tipoCliente, empleadoVentas, limCre, prioridad, listaPrecio, indicador, zona,
			creadoMovil, claveMovil, estadoRegistroMovil, direccionFiscal, TransaccionMovil,ValidoenPedido;
	
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

	private ArrayList<ContactoBean> contactos;
	private ArrayList<DireccionBean> direcciones;
	

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getEmpleadoVentas() {
		return empleadoVentas;
	}

	public void setEmpleadoVentas(String empleadoVentas) {
		this.empleadoVentas = empleadoVentas;
	}

	public String getLimCre() {
		return limCre;
	}

	public void setLimCre(String limCre) {
		this.limCre = limCre;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}

	public String getNombRazSoc() {
		return nombRazSoc;
	}

	public void setNombRazSoc(String nombRazSoc) {
		this.nombRazSoc = nombRazSoc;
	}

	public String getApePat() {
		return apePat;
	}

	public void setApePat(String apePat) {
		this.apePat = apePat;
	}

	public String getApeMat() {
		return apeMat;
	}

	public void setApeMat(String apeMat) {
		this.apeMat = apeMat;
	}

	public String getPriNom() {
		return priNom;
	}

	public void setPriNom(String priNom) {
		this.priNom = priNom;
	}

	public String getSegNom() {
		return segNom;
	}

	public void setSegNom(String segNom) {
		this.segNom = segNom;
	}

	public String getNomCom() {
		return nomCom;
	}

	public void setNomCom(String nomCom) {
		this.nomCom = nomCom;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getTlf1() {
		return tlf1;
	}

	public void setTlf1(String tlf1) {
		this.tlf1 = tlf1;
	}

	public String getTlf2() {
		return tlf2;
	}

	public void setTlf2(String tlf2) {
		this.tlf2 = tlf2;
	}

	public String getTlfMov() {
		return tlfMov;
	}

	public void setTlfMov(String tlfMov) {
		this.tlfMov = tlfMov;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	

	public String getCondPago() {
		return condPago;
	}

	public void setCondPago(String condPago) {
		this.condPago = condPago;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public ArrayList<ContactoBean> getContactos() {
		return contactos;
	}

	public void setContactos(ArrayList<ContactoBean> contactos) {
		this.contactos = contactos;
	}

	public ArrayList<DireccionBean> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(ArrayList<DireccionBean> direcciones) {
		this.direcciones = direcciones;
	}

	public String getListaPrecio() {
		return listaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getDireccionFiscal() {
		return direccionFiscal;
	}

	public void setDireccionFiscal(String direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}

	public String getTransaccionMovil() {
		return TransaccionMovil;
	}

	public void setTransaccionMovil(String transaccionMovil) {
		TransaccionMovil = transaccionMovil;
	}

	public String getValidoenPedido() {
		return ValidoenPedido;
	}

	public void setValidoenPedido(String validoenPedido) {
		ValidoenPedido = validoenPedido;
	}
			
	
	


}
