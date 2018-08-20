package com.proyecto.bean;

public class ArticuloBean {

	private String cod, desc, fabricante, grupoArticulo, codUM,unidadMedidaVenta, 
			nroCatIC, peso, almacen, fecVen, gDet, marca,
			zona, codProv, stock, codigoImpuesto, utilLinea, codigoListaPrecio, descripcionListaPrecio,
	almacenDefecto;
	
	private String nombreFabricante, nombreGrupoArt, nombreUnidadMedida, nombreUnidadMedidaVenta;

	public String getCodigoListaPrecio() {
		return codigoListaPrecio;
	}

	public void setCodigoListaPrecio(String codigoListaPrecio) {
		this.codigoListaPrecio = codigoListaPrecio;
	}

	public String getDescripcionListaPrecio() {
		return descripcionListaPrecio;
	}

	public void setDescripcionListaPrecio(String descripcionListaPrecio) {
		this.descripcionListaPrecio = descripcionListaPrecio;
	}

	public String getNombreFabricante() {
		return nombreFabricante;
	}

	public void setNombreFabricante(String nombreFabricante) {
		this.nombreFabricante = nombreFabricante;
	}

	public String getNombreGrupoArt() {
		return nombreGrupoArt;
	}

	public void setNombreGrupoArt(String nombreGrupoArt) {
		this.nombreGrupoArt = nombreGrupoArt;
	}

	public String getNombreUnidadMedida() {
		return nombreUnidadMedida;
	}

	public void setNombreUnidadMedida(String nombreUnidadMedida) {
		this.nombreUnidadMedida = nombreUnidadMedida;
	}

	public String getNombreUnidadMedidaVenta() {
		return nombreUnidadMedidaVenta;
	}

	public void setNombreUnidadMedidaVenta(String nombreUnidadMedidaVenta) {
		this.nombreUnidadMedidaVenta = nombreUnidadMedidaVenta;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getGrupoArticulo() {
		return grupoArticulo;
	}

	public void setGrupoArticulo(String grupoArticulo) {
		this.grupoArticulo = grupoArticulo;
	}

	public String getUnidadMedidaVenta() {
		return unidadMedidaVenta;
	}

	public void setUnidadMedidaVenta(String unidadMedidaVenta) {
		this.unidadMedidaVenta = unidadMedidaVenta;
	}

	private double pre, descuento, impuesto, preBruto, cant;

	private int cantPen, artXuni, utilIcon;

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNroCatIC() {
		return nroCatIC;
	}

	public void setNroCatIC(String nroCatIC) {
		this.nroCatIC = nroCatIC;
	}

	public String getCodUM() {
		return codUM;
	}

	public void setCodUM(String codUM) {
		this.codUM = codUM;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	public String getFecVen() {
		return fecVen;
	}

	public void setFecVen(String fecVen) {
		this.fecVen = fecVen;
	}

	public String getgDet() {
		return gDet;
	}

	public void setgDet(String gDet) {
		this.gDet = gDet;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public double getPre() {
		double num = pre;
		return Math.round(num * 100.0) / 100.0;
	}

	public void setPre(double pre) {
		this.pre = pre;
	}

	public double getDescuento() {
		double num = descuento / 100;
		return Math.round(num * 100.0) / 100.0;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getImpuesto() {
		double num = impuesto / 100;
		return Math.round(num * 100.0) / 100.0;
	}

	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}

	public double getPreBruto() {

		preBruto = this.pre - (this.pre * getDescuento())
				+ ((this.pre - (this.pre * getDescuento())) * getImpuesto());
		return Math.round(preBruto * 100.0) / 100.0;

	}

	public void setPreBruto(double preBruto) {
		this.preBruto = preBruto;
	}

	public double getTotal() {
		return Math.round(((pre * cant) - (pre * cant) * getDescuento()) * 100d) / 100d;
	}

	public double getCant() {
		return cant;
	}

	public void setCant(double cant) {
		this.cant = cant;
	}

	public int getCantPen() {
		return cantPen;
	}

	public void setCantPen(int cantPen) {
		this.cantPen = cantPen;
	}

	public int getArtXuni() {
		return artXuni;
	}

	public void setArtXuni(int artXuni) {
		this.artXuni = artXuni;
	}

	public int getUtilIcon() {
		return utilIcon;
	}

	public void setUtilIcon(int utilIcon) {
		this.utilIcon = utilIcon;
	}

	public String getCodProv() {
		return codProv;
	}

	public void setCodProv(String codProv) {
		this.codProv = codProv;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getCodigoImpuesto() {
		return codigoImpuesto;
	}

	public void setCodigoImpuesto(String codigoImpuesto) {
		this.codigoImpuesto = codigoImpuesto;
	}

	public String getUtilLinea() {
		return utilLinea;
	}

	public void setUtilLinea(String utilLinea) {
		this.utilLinea = utilLinea;
	}

	public String getAlmacenDefecto() {
		return almacenDefecto;
	}

	public void setAlmacenDefecto(String almacenDefecto) {
		this.almacenDefecto = almacenDefecto;
	}
}
