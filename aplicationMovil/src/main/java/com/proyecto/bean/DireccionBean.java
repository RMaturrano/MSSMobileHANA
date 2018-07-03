package com.proyecto.bean;

import org.json.JSONObject;

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
	private String visitaLunes;
	private String visitaMartes;
	private String visitaMiercoles;
	private String visitaJueves;
	private String visitaViernes;
	private String visitaSabado;
	private String visitaDomingo;
	private String frecuenciaVisita;
	private String ruta;
	private String zona;
	private String canal;
	private String giro;
	private String fechaInicioVisitas;
	private String vendedor;
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

	public String getVisitaLunes() {
		return visitaLunes;
	}

	public void setVisitaLunes(String visitaLunes) {
		this.visitaLunes = visitaLunes;
	}

	public String getVisitaMartes() {
		return visitaMartes;
	}

	public void setVisitaMartes(String visitaMartes) {
		this.visitaMartes = visitaMartes;
	}

	public String getVisitaMiercoles() {
		return visitaMiercoles;
	}

	public void setVisitaMiercoles(String visitaMiercoles) {
		this.visitaMiercoles = visitaMiercoles;
	}

	public String getVisitaJueves() {
		return visitaJueves;
	}

	public void setVisitaJueves(String visitaJueves) {
		this.visitaJueves = visitaJueves;
	}

	public String getVisitaViernes() {
		return visitaViernes;
	}

	public void setVisitaViernes(String visitaViernes) {
		this.visitaViernes = visitaViernes;
	}

	public String getVisitaSabado() {
		return visitaSabado;
	}

	public void setVisitaSabado(String visitaSabado) {
		this.visitaSabado = visitaSabado;
	}

	public String getVisitaDomingo() {
		return visitaDomingo;
	}

	public void setVisitaDomingo(String visitaDomingo) {
		this.visitaDomingo = visitaDomingo;
	}

	public String getFrecuenciaVisita() {
		return frecuenciaVisita;
	}

	public void setFrecuenciaVisita(String frecuenciaVisita) {
		this.frecuenciaVisita = frecuenciaVisita;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getFechaInicioVisitas() {
		return fechaInicioVisitas;
	}

	public void setFechaInicioVisitas(String fechaInicioVisitas) {
		this.fechaInicioVisitas = fechaInicioVisitas;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getGiro() {
		return giro;
	}

	public void setGiro(String giro) {
		this.giro = giro;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public static JSONObject transformToJSON(DireccionBean direccionBean){

		JSONObject jsonObject = null;
		try{

			jsonObject = new JSONObject();
			jsonObject.put("ClaveMovil", direccionBean.getCodigoCliente());
			jsonObject.put("CodigoCliente", direccionBean.getCodigoCliente());
			jsonObject.put("CodigoDireccion", direccionBean.getIDDireccion());
			jsonObject.put("Latitud", direccionBean.getLatitud());
			jsonObject.put("Longitud", direccionBean.getLongitud());
			jsonObject.put("Tipo", direccionBean.getTipoDireccion());

		}catch (Exception e){
			jsonObject = null;
		}

		return  jsonObject;
	}

}
