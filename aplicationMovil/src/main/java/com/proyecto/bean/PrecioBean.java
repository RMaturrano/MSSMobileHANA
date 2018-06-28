package com.proyecto.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PrecioBean implements Parcelable {

	private String listaPrecio;
	private String articulo;
	private String moneda;
	private String precioVenta;

	public  PrecioBean(){};

	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(String precioVenta) {
		this.precioVenta = precioVenta;
	}

	public String getListaPrecio() {
		return listaPrecio;
	}

	public void setListaPrecio(String listaPrecio) {
		this.listaPrecio = listaPrecio;
	}


	protected PrecioBean(Parcel in) {
		listaPrecio = in.readString();
		articulo = in.readString();
		moneda = in.readString();
		precioVenta = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(listaPrecio);
		dest.writeString(articulo);
		dest.writeString(moneda);
		dest.writeString(precioVenta);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<PrecioBean> CREATOR = new Parcelable.Creator<PrecioBean>() {
		@Override
		public PrecioBean createFromParcel(Parcel in) {
			return new PrecioBean(in);
		}

		@Override
		public PrecioBean[] newArray(int size) {
			return new PrecioBean[size];
		}
	};
}
