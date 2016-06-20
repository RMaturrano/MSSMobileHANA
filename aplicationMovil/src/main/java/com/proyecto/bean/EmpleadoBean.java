package com.proyecto.bean;


public class EmpleadoBean {
	
	private String CodigoVendedor, NombreVendedor, CodigoUsuario,ClaveUsuario, 
					MovilId, MovilEditar, MovilAprobar,MovilCrear,MovilRechazar,MovilEscogerPrecio;

	@Override
	public String toString() {
		return this.NombreVendedor;
	}
	
	
	public String getCodigoVendedor() {
		return CodigoVendedor;
	}

	public void setCodigoVendedor(String codigoVendedor) {
		CodigoVendedor = codigoVendedor;
	}

	public String getNombreVendedor() {
		return NombreVendedor;
	}

	public void setNombreVendedor(String nombreVendedor) {
		NombreVendedor = nombreVendedor;
	}

	public String getCodigoUsuario() {
		return CodigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		CodigoUsuario = codigoUsuario;
	}

	public String getClaveUsuario() {
		return ClaveUsuario;
	}

	public void setClaveUsuario(String claveUsuario) {
		ClaveUsuario = claveUsuario;
	}

	public String getMovilId() {
		return MovilId;
	}

	public void setMovilId(String movilId) {
		MovilId = movilId;
	}

	public String getMovilEditar() {
		return MovilEditar;
	}

	public void setMovilEditar(String movilEditar) {
		MovilEditar = movilEditar;
	}

	public String getMovilAprobar() {
		return MovilAprobar;
	}

	public void setMovilAprobar(String movilAprobar) {
		MovilAprobar = movilAprobar;
	}

	public String getMovilCrear() {
		return MovilCrear;
	}

	public void setMovilCrear(String movilCrear) {
		MovilCrear = movilCrear;
	}

	public String getMovilRechazar() {
		return MovilRechazar;
	}

	public void setMovilRechazar(String movilRechazar) {
		MovilRechazar = movilRechazar;
	}

	public String getMovilEscogerPrecio() {
		return MovilEscogerPrecio;
	}

	public void setMovilEscogerPrecio(String movilEscogerPrecio) {
		MovilEscogerPrecio = movilEscogerPrecio;
	}
	
	
	

}
