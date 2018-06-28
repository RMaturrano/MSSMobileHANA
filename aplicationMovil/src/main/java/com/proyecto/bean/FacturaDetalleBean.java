package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FacturaDetalleBean implements Parcelable {

	private int Linea;
	private String articulo;
	private String ArticuloNombre;
	private String unidadMedida;
	private String almacen;
	private String AlmacenNombre;
	private String cantidad;
	private String Diponible;
	private String listaPrecio;
	private String precioUnitario;
	private String porcentajeDescuento;
	private String impuesto;
	private String fechaEntrega;
	boolean selected;
	private String CantidadTemp;
	private List<FacturaDetalleLoteBean> Lotes;

	public  FacturaDetalleBean(){};

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getListaPrecio() {
		return listaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}

	public String getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(String precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(String porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public String getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}

	public String getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public int getLinea() {
		return Linea;
	}

	public void setLinea(int linea) {
		Linea = linea;
	}

	public String getArticuloNombre() {
		return ArticuloNombre;
	}

	public void setArticuloNombre(String articuloNombre) {
		ArticuloNombre = articuloNombre;
	}

	public String getAlmacenNombre() {
		return AlmacenNombre;
	}

	public void setAlmacenNombre(String almacenNombre) {
		AlmacenNombre = almacenNombre;
	}

	public String getDiponible() {
		return Diponible;
	}

	public void setDiponible(String diponible) {
		Diponible = diponible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCantidadTemp() {
		return CantidadTemp;
	}

	public void setCantidadTemp(String cantidadTemp) {
		CantidadTemp = cantidadTemp;
	}

	public List<FacturaDetalleLoteBean> getLotes() {
		return Lotes;
	}

	public void setLotes(List<FacturaDetalleLoteBean> lotes) {
		Lotes = lotes;
	}

	protected FacturaDetalleBean(Parcel in) {
		Linea = in.readInt();
		articulo = in.readString();
		ArticuloNombre = in.readString();
		unidadMedida = in.readString();
		almacen = in.readString();
		AlmacenNombre = in.readString();
		cantidad = in.readString();
		Diponible = in.readString();
		listaPrecio = in.readString();
		precioUnitario = in.readString();
		porcentajeDescuento = in.readString();
		impuesto = in.readString();
		fechaEntrega = in.readString();
		selected = in.readByte() != 0x00;
		CantidadTemp = in.readString();
		if (in.readByte() == 0x01) {
			Lotes = new ArrayList<FacturaDetalleLoteBean>();
			in.readList(Lotes, FacturaDetalleLoteBean.class.getClassLoader());
		} else {
			Lotes = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Linea);
		dest.writeString(articulo);
		dest.writeString(ArticuloNombre);
		dest.writeString(unidadMedida);
		dest.writeString(almacen);
		dest.writeString(AlmacenNombre);
		dest.writeString(cantidad);
		dest.writeString(Diponible);
		dest.writeString(listaPrecio);
		dest.writeString(precioUnitario);
		dest.writeString(porcentajeDescuento);
		dest.writeString(impuesto);
		dest.writeString(fechaEntrega);
		dest.writeByte((byte) (selected ? 0x01 : 0x00));
		dest.writeString(CantidadTemp);
		if (Lotes == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(Lotes);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<FacturaDetalleBean> CREATOR = new Parcelable.Creator<FacturaDetalleBean>() {
		@Override
		public FacturaDetalleBean createFromParcel(Parcel in) {
			return new FacturaDetalleBean(in);
		}

		@Override
		public FacturaDetalleBean[] newArray(int size) {
			return new FacturaDetalleBean[size];
		}
	};
}